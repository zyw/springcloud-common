package cn.v5cn.cim.route.service.impl;

import cn.v5cn.cim.common.core.proxy.ProxyManager;
import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.pojo.CIMUserInfo;
import cn.v5cn.cim.common.util.RouteInfoParseUtil;
import cn.v5cn.cim.route.api.vo.req.ChatReqVO;
import cn.v5cn.cim.route.api.vo.req.LoginReqVO;
import cn.v5cn.cim.route.api.vo.res.CIMServerResVO;
import cn.v5cn.cim.route.api.vo.res.RegisterInfoResVO;
import cn.v5cn.cim.route.constant.Constant;
import cn.v5cn.cim.route.service.AccountService;
import cn.v5cn.cim.route.service.UserInfoCacheService;
import cn.v5cn.cim.server.api.ServerApi;
import cn.v5cn.cim.server.api.vo.req.SendMsgReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author crossoverJie
 */
@Service
public class AccountServiceRedisImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public RegisterInfoResVO register(RegisterInfoResVO info) throws Exception {
        String key = Constant.ACCOUNT_PREFIX + info.getUserId();

        String name = redisTemplate.opsForValue().get(info.getUserName());
        if(null == name) {
            //为了方便查询，冗余一份
            redisTemplate.opsForValue().set(key, info.getUserName());
            redisTemplate.opsForValue().set(info.getUserName(), key);
        } else {
            long userId = Long.parseLong(name.split(":")[1]);
            info.setUserId(userId);
            info.setUserName(info.getUserName());
        }
        return info;
    }

    @Override
    public StatusEnum login(LoginReqVO loginReqVO) throws Exception {
        //再去Redis里查询
        String key = Constant.ACCOUNT_PREFIX + loginReqVO.getUserId();
        String userName = redisTemplate.opsForValue().get(key);
        if(null == userName) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        if (!userName.equals(loginReqVO.getUserName())) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        boolean status = userInfoCacheService.saveAndCheckUserLoginStatus(loginReqVO.getUserId());
        if (!status) {
            return StatusEnum.REPEAT_LOGIN;
        }

        return StatusEnum.SUCCESS;
    }

    @Override
    public void saveRouteInfo(LoginReqVO loginReqVO, String msg) throws Exception {
        String key = Constant.ROUTE_PREFIX + loginReqVO.getUserId();
        redisTemplate.opsForValue().set(key, msg);
    }

    @Override
    public Map<Long, CIMServerResVO> loadRouteRelated() {

        Map<Long, CIMServerResVO> routes = new HashMap<>(64);

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match(Constant.ROUTE_PREFIX + "*")
                .build();

        Cursor<byte[]> scan = connection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            LOGGER.info("key={}", key);
            parseServerInfo(routes, key);
        }
        try {
            scan.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        return routes;
    }

    @Override
    public CIMServerResVO loadRouteRelatedByUserId(Long userId) {
        String value = redisTemplate.opsForValue().get(Constant.ROUTE_PREFIX + userId);

        if (value == null) {
            throw new CIMException(StatusEnum.OFF_LINE);
        }

        return new CIMServerResVO(RouteInfoParseUtil.parse(value));
    }

    private void parseServerInfo(Map<Long, CIMServerResVO> routes, String key) {
        Long userId = Long.valueOf(key.split(":")[1]);
        String value = redisTemplate.opsForValue().get(key);
        CIMServerResVO cimServerResVO = new CIMServerResVO(RouteInfoParseUtil.parse(value));
        routes.put(userId, cimServerResVO);
    }

    @Override
    public void pushMsg(CIMServerResVO cimServerResVO, long sendUserId, ChatReqVO groupReqVO) throws Exception {
        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(sendUserId);

        String url = "http://" + cimServerResVO.getIp() + ":" + cimServerResVO.getHttpPort();
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO vo = new SendMsgReqVO(cimUserInfo.getUserName() + ":" + groupReqVO.getMsg(), groupReqVO.getUserId());
        Response response = null;
        try {
            response = (Response) serverApi.sendMsg(vo);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public void offLine(Long userId) throws Exception {
        // TODO: 2019-01-21 改为一个原子命令，以防数据一致性

        //删除路由
        redisTemplate.delete(Constant.ROUTE_PREFIX + userId);

        //删除登录状态
        userInfoCacheService.removeLoginStatus(userId);
    }
}

package cn.v5cn.cim.server.kit;

import cn.v5cn.cim.common.core.proxy.ProxyManager;
import cn.v5cn.cim.common.pojo.CIMUserInfo;
import cn.v5cn.cim.route.api.RouteApi;
import cn.v5cn.cim.route.api.vo.req.ChatReqVO;
import cn.v5cn.cim.server.config.AppConfiguration;
import cn.v5cn.cim.server.util.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author crossoverJie
 */
@Component
public class RouteHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteHandler.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private AppConfiguration configuration;

    /**
     * 用户下线
     * @param userInfo
     * @param channel
     * @throws IOException
     */
    public void userOffLine(CIMUserInfo userInfo, NioSocketChannel channel) throws IOException {
        if(userInfo != null) {
            LOGGER.info("Account [{}] offline", userInfo.getUserName());
            SessionSocketHolder.removeSession(userInfo.getUserId());
            // 清除路由关系
            clearRouteInfo(userInfo);
        }
        SessionSocketHolder.remove(channel);
    }

    /**
     * 清除路由关系
     * @param userInfo
     */
    public void clearRouteInfo(CIMUserInfo userInfo) {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, configuration.getRouteUrl(), okHttpClient).getInstance();
        Response response = null;
        ChatReqVO vo = new ChatReqVO(userInfo.getUserId(), userInfo.getUserName());
        try {
            response = (Response) routeApi.offLine(vo);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            response.body().close();
        }
    }
}

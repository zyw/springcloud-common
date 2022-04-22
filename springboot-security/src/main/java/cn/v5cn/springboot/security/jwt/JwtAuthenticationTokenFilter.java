package cn.v5cn.springboot.security.jwt;

import cn.v5cn.springboot.security.handler.MyAuthenticationException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 *
 * @FileName: JwtAuthenticationTokenFilter
 * @Company:
 * @author    ljy
 * @Date      2018年05月120日
 * @version   1.0.0
 * @remark:   jwt认证token
 * @explain   每次请求接口时 就会进入这里验证token 是否合法
 *             token 如果用户一直在操作，则token 过期时间会叠加    如果超过设置的过期时间未操作  则token 失效 需要重新登录
 *
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Resource(name = "myUserDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate redisUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.expiration}")
    private Long expiration;

    private RequestMatcher authenticationRequestMatcher;

    public JwtAuthenticationTokenFilter() {
    }

    public JwtAuthenticationTokenFilter(RequestMatcher authenticationRequestMatcher) {
        this.authenticationRequestMatcher = authenticationRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //过滤掉不需要token验证的url
        if(authenticationRequestMatcher != null && !authenticationRequestMatcher.matches(request)){
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            LOGGER.info("请求"+request.getRequestURI()+"携带的token值：" + authToken);
            // 查看redis中的token信息是否过期
            redisUtil.boundValueOps("dddd").set(authToken,7, TimeUnit.DAYS);
            //if (!isExists){
                // token 过期 提示用户登录超时 重新登录系统
                //throw  new MyAuthenticationException(ErrorCodeEnum.LOGIN_WITHOUT);
            //}

            //如果在token过期之前触发接口,我们更新token过期时间，token值不变只更新过期时间
            Date createTokenDate = jwtTokenUtil.getCreatedDateFromToken(authToken);  //获取token生成时间
            LOGGER.info("createTokenDate: " + createTokenDate);
            if(createTokenDate != null){
                Duration between = Duration.between(Instant.now(), Instant.ofEpochMilli(createTokenDate.getTime()));
                Long differSeconds = between.toMillis();
                boolean isExpire = expiration > differSeconds;
                if (isExpire) {  //如果 请求接口时间在token 过期之前 则更新token过期时间  我们可以将用户的token 存放到redis 中,更新redis 的过期时间

                    //更新 延长 redis 中的过期时间


                }
            }


            String useraccount = jwtTokenUtil.getUsernameFromToken(authToken);
            LOGGER.info("JwtAuthenticationTokenFilter[doFilterInternal] checking authentication " + useraccount);

            //token校验通过
            if(useraccount != null){
                //TokenUtils.setToken(authToken);//设置token
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null  || authentication.getPrincipal().equals("anonymousUser")){
                    //根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(useraccount);

                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                request));
                        LOGGER.info("JwtAuthenticationTokenFilter[doFilterInternal]  authenticated user " + useraccount + ", setting security context");
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
                    }
                }else {
                    LOGGER.info("当前请求用户信息："+ JSONObject.toJSONString(authentication.getPrincipal()));
                }
            }else {
                LOGGER.info("token 无效.");
                throw  new MyAuthenticationException("token无效");
            }
        }
        filterChain.doFilter(request, response);
    }
}

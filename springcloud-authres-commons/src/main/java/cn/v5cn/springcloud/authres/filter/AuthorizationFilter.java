package cn.v5cn.springcloud.authres.filter;

import cn.v5cn.springcloud.authres.commons.AccessType;
import cn.v5cn.springcloud.authres.security.CustomAuthentication;
import cn.v5cn.springcloud.authres.security.SimpleGrantedAuthority;
import cn.v5cn.springcloud.authres.security.UserContext;
import cn.v5cn.springcloud.commons.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZYW
 */
public class AuthorizationFilter implements Filter {

    private static final Log LOGGER = LogFactory.getLog(AuthorizationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("初始化过滤器...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("过滤器正在执行...");
        String userId = ((HttpServletRequest) servletRequest).getHeader(SecurityConstants.USER_ID_IN_HEADER);
        if(StringUtils.isNotEmpty(userId)) {
            UserContext userContext = new UserContext(Long.valueOf(userId));
            userContext.setAccessType(AccessType.ACCESS_TYPE_NORMAL);
            //TODO 根据用户ID查询用户权限
            //List<Permission> permissionList = feignAuthClient.getUserPermissions(userId);
            List<SimpleGrantedAuthority> authorityList = new ArrayList();
            /*for (Permission permission : permissionList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority();
                authority.setAuthority(permission.getPermission());
                authorityList.add(authority);
            }*/
            SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority();
            authority1.setAuthority("user_list");
            SimpleGrantedAuthority authority2 = new SimpleGrantedAuthority();
            authority2.setAuthority("user_add");

            authorityList.add(authority1);
            authorityList.add(authority2);

            CustomAuthentication userAuth = new CustomAuthentication();
            userAuth.setAuthorities(authorityList);

            userContext.setAuthorities(authorityList);
            userContext.setAuthentication(userAuth);

            SecurityContextHolder.setContext(userContext);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}

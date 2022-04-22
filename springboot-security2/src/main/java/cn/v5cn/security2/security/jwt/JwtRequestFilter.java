package cn.v5cn.security2.security.jwt;

import cn.v5cn.security2.security.service.UserInfoService;
import cn.v5cn.security2.security.util.ResultUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:32
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private RequestMatcher authenticationRequestMatcher;

    public JwtRequestFilter() {
    }

    public JwtRequestFilter(RequestMatcher authenticationRequestMatcher) {
        this.authenticationRequestMatcher = authenticationRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //过滤掉不需要token验证的url
        if(authenticationRequestMatcher != null && !authenticationRequestMatcher.matches(request)){
            chain.doFilter(request, response);
            return;
        }
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // JWT令牌的形式为"Bearer token". 删除Bearer单词并获得令牌
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
                ResultUtil.result(response, HttpStatus.BAD_REQUEST,"1002","Unable to get JWT Token");
                return;
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
                ResultUtil.result(response, HttpStatus.BAD_REQUEST,"1003","JWT Token has expired");
                return;
            }
        } else {
            //throw new BadCredentialsException("没有传递令牌或者令牌格式错误");
            ResultUtil.result(response, HttpStatus.BAD_REQUEST,"1001","没有传递令牌或者令牌格式错误");
            return;
        }
        // 一旦我们获得令牌，请对其进行验证。
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userInfoService.loadUserByUsername(username);
            // 如果令牌有效，则将Spring Security配置为手动设置身份验证
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 在上下文中设置身份验证后，我们指定当前用户已通过身份验证。 因此它成功通过了Spring Security Configurations。
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }


}

package cn.v5cn.security2.security;

import cn.v5cn.security2.security.entity.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


public final class SecurityUtils {

    public static String getCurrentUserUsername() {
        UsernamePasswordAuthenticationToken userContext = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)userContext.getPrincipal();
        return userDetails.getUsername();
    }

    public static Long getCurrentUserId() {
//        UserContext userContext = (UserContext) SecurityContextHolder.getContext();
//        return userContext.getUserId();
        return 0L;
    }

    public static Long getCustomerId() {
//        UserContext userContext = (UserContext) SecurityContextHolder.getContext();
//        return userContext.getCustomerId();
        return 0L;
    }
}

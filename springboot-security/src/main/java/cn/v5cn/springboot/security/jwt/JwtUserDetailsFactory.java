package cn.v5cn.springboot.security.jwt;

import cn.v5cn.springboot.security.entity.UserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/***
 *
 * @FileName: JWTUserDetailsFactory
 * @Company:
 * @author    ljy
 * @Date      2018年05月120日
 * @version   1.0.0
 * @remark:   负责创建JWTUserDetails 对象
 *
 */
public class JwtUserDetailsFactory {
    private JwtUserDetailsFactory(){

    }

    public static JwtUserDetails create(User user, Long userId, Instant date){
        return new JwtUserDetails(userId, user.getUsername(), user.getPassword(),user.getAuthorities(), date);
    }

    public static JwtUserDetails create(User user, UserDetail userDetail){
        return new JwtUserDetails(userDetail,user.getAuthorities());
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

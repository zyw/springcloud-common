package cn.v5cn.security2.security.service;

import cn.v5cn.security2.security.SimpleGrantedAuthority;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.v5cn.security2.security.entity.UserDetailsImpl;

import java.util.Collection;
import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:53
 */
@Service
public class UserInfoService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorityList = Lists.newArrayList();
        SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority();
        authority1.setAuthority("user_list");
        SimpleGrantedAuthority authority2 = new SimpleGrantedAuthority();
        authority2.setAuthority("user_add");

        authorityList.add(authority1);
        authorityList.add(authority2);
        return new UserDetailsImpl("zhangsan","$2a$10$5uqHclwPzmdLJbkjfsoJIu4BjHP.UCbB7p4Ok1zwzsRA7X.VasqQ.",authorityList);
    }
}

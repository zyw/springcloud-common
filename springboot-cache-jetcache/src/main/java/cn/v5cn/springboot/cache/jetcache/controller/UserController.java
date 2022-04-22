package cn.v5cn.springboot.cache.jetcache.controller;

import cn.v5cn.springboot.cache.jetcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public Object index(){
        return userService.findUser();
    }
}

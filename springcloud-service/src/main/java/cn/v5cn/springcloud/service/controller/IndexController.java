package cn.v5cn.springcloud.service.controller;

import cn.v5cn.springcloud.authres.annotation.PreAuth;
import cn.v5cn.springcloud.commons.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController extends BaseController {

    @PreAuth("hasAuthority('user_list')")
    @GetMapping("/index")
    public Object index() {
        return success("Hello index!");
    }

    @GetMapping("/index2")
    public Object index2() {
        return success("Hello index2!");
    }

    @PreAuth("hasAuthority('user_view')")
    @GetMapping("/index3")
    public Object index3() {
        return success("Hello index3!");
    }
}

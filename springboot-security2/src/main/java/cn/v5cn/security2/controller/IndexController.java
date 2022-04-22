package cn.v5cn.security2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:15
 */
@RestController
public class IndexController {

    @PreAuthorize("hasAuthority('user_list')")
    @RequestMapping({ "/hello" })
    public String firstPage() {
        return "Hello World";
    }
}

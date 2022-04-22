package cn.v5cn.springboot.logstash.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static net.logstash.logback.marker.Markers.appendFields;

@RestController
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/index")
    public Object index(String id) {
        User user = new User();
        user.setId(id);
        user.setName("zhangsan" + id);
        user.setAge(21);
        LOGGER.info(appendFields(user),"log");

        return user;
    }
}

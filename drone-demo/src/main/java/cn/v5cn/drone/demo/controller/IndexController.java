package cn.v5cn.drone.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class IndexController {

    @GetMapping("/index")
    public Object index() throws IOException {

        File flie = new File("/mnt/data/hello.txt");
        System.out.println(flie.getParent() + "--------------------------------------");
        if(!flie.exists()) {
            System.out.println(flie.getParent() + "--------------------------------------");
            if(!flie.getParentFile().exists()) {
                flie.getParentFile().mkdirs();
            }
            flie.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(flie,true);
        fos.write("Hello World".getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        return "Hello Drone 成功 ~~~~~~";
    }
}

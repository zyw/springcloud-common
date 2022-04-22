package cn.v5cn.springcloud.zuul.controller;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.codec.binary.Base64;
import org.orclight.java.util.captha.CaptchaClient;
import org.orclight.java.util.captha.bean.CaptchaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zyw
 * @date 2018/2/9
 */
@RestController
public class CaptchaController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CaptchaClient captchaClient;

    @GetMapping("/captcha")
    public Object captcha() {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            CaptchaBean captchaBean = captchaClient.generate();
            String capText = captchaBean.getResult();
            String uuid = UUID.randomUUID().toString();
            stringRedisTemplate.boundValueOps(uuid).set(capText,60, TimeUnit.SECONDS);
            BufferedImage bi = captchaBean.getBufferedImage();
            ImageIO.write(bi, "jpg", baos);
            String imgBase64 = Base64.encodeBase64String(baos.toByteArray());

            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin","*")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            ImmutableMap.of("data", ImmutableMap.of(uuid,"data:image/jpeg;base64,"+imgBase64)
                                    ,"code", 200
                                    ,"msg", "成功！"
                                    ,"timestamp", System.currentTimeMillis())
                    );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

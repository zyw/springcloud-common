package cn.v5cn.captcha.demo.controller;

import cn.v5cn.captcha.demo.vo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author powertime
 */
@RestController
public class MiCaptchaController {
    @GetMapping("/v1/captcha/init")
    public ResponseEntity<InitResp> init() {
        InitResp resp = new InitResp();
        InitRespData data = new InitRespData();
        InitRespRet ret = new InitRespRet();

        resp.setRet(ret);
        resp.setData(data);

        ret.setCode(1);
        data.setKey(UUID.randomUUID().toString());

        return ResponseEntity.ok(resp);
    }

    @PostMapping("v1/captcha/check")
    public ResponseEntity<CheckResp> check() {
        CheckResp resp = new CheckResp();
        resp.setPass(false);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("v1/captcha/verification")
    public ResponseEntity<VerificationResp> verification() {
        VerificationResp resp = new VerificationResp();
        VerificationRespRet ret = new VerificationRespRet();

        ret.setCode(1);
        resp.setRet(ret);
        resp.setData("HelloWorld");
        resp.setUuid("123456789");
        return ResponseEntity.ok(resp);
    }
}

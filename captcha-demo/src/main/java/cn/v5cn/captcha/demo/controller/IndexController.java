package cn.v5cn.captcha.demo.controller;

import cloud.tianai.captcha.slider.SliderCaptchaApplication;
import cloud.tianai.captcha.template.slider.validator.SliderCaptchaTrack;
import cloud.tianai.captcha.vo.CaptchaResponse;
import cloud.tianai.captcha.vo.SliderCaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author powertime
 */
@Controller
public class IndexController {

    @Autowired
    private SliderCaptchaApplication sliderCaptchaApplication;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/gen")
    @ResponseBody
    public CaptchaResponse<SliderCaptchaVO> genCaptcha(HttpServletRequest request) {
        CaptchaResponse<SliderCaptchaVO> response = sliderCaptchaApplication.generateSliderCaptcha();
        return response;
    }

    @PostMapping("/check")
    @ResponseBody
    public boolean checkCaptcha(@RequestParam("id") String id,
                                @RequestBody SliderCaptchaTrack sliderCaptchaTrack,
                                HttpServletRequest request) {
        return sliderCaptchaApplication.matching(id, sliderCaptchaTrack);
    }
}

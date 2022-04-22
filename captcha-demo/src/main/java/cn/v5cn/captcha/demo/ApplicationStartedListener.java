package cn.v5cn.captcha.demo;

import cloud.tianai.captcha.slider.SliderCaptchaApplication;
import cloud.tianai.captcha.template.slider.Resource;
import cloud.tianai.captcha.template.slider.ResourceStore;
import cloud.tianai.captcha.template.slider.SliderCaptchaResourceManager;
import cloud.tianai.captcha.template.slider.provider.ClassPathResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author powertime
 */
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private SliderCaptchaApplication sliderCaptchaApplication;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SliderCaptchaResourceManager sliderCaptchaResourceManager = sliderCaptchaApplication.getSliderCaptchaResourceManager();
        ResourceStore resourceStore = sliderCaptchaResourceManager.getResourceStore();
        // 清除内置的背景图片
        resourceStore.clearResources();

        // 添加自定义背景图片
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/a.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/b.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/c.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/d.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/e.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/g.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/h.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/i.jpg"));
        resourceStore.addResource(new Resource(ClassPathResourceProvider.NAME, "bgimages/j.jpg"));
    }
}

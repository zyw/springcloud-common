package cn.v5cn.springcloud.zuul;

import org.orclight.java.util.captha.CaptchaClient;
import org.orclight.java.util.captha.strategy.SimpleCaptchaStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringCloudApplication
@EnableZuulProxy
@EnableHystrix
public class ZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}

	@Bean
	public CaptchaClient captchaClient(){
		return CaptchaClient.create()
				.captchaStrategy(new SimpleCaptchaStrategy())
				.build();
	}
}

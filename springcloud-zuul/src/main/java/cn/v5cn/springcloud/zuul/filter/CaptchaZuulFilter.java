package cn.v5cn.springcloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zyw
 * @date 2018/2/9
 */
@Component
public class CaptchaZuulFilter extends ZuulFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getRequestURI().contains("/oauth/token");
    }

    @Override
    public Object run() {

        /*RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String captcha = req.getParameter("captcha");
        String captchaKey = req.getParameter("captchaKey");

        if(StringUtils.isEmpty(captcha) || StringUtils.isEmpty(captchaKey)) {
            throw new ZuulRuntimeException(new ZuulException(HttpStatus.V5_CAPTCHA_NULL.msg(), HttpStatus.V5_CAPTCHA_NULL.value(),HttpStatus.V5_CAPTCHA_NULL.msg()));
        }

        String redisCaptcha = stringRedisTemplate.boundValueOps(captchaKey).get();

        if(!StringUtils.equalsIgnoreCase(captcha,redisCaptcha)) {
            throw new ZuulRuntimeException(new ZuulException(HttpStatus.V5_CAPTCHA_ERROR.msg(), HttpStatus.V5_CAPTCHA_ERROR.value(),HttpStatus.V5_CAPTCHA_ERROR.msg()));
        }*/

        return null;
    }
}

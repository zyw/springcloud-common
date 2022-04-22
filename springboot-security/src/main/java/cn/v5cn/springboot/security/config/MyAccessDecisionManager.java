package cn.v5cn.springboot.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;

/***
 *
 * @FileName: MyAccessDecisionManager
 * @Company:
 * @author
 * @Date      2018年05月11日
 * @version   1.0.0
 * @remark:   资源权限认证器  证用户是否拥有所请求资源的权限
 * @explain   接口AccessDecisionManager也是必须实现的。
 *            decide方法里面写的就是授权策略了，需要什么策略，可以自己写其中的策略逻辑
 *            认证通过就返回，不通过抛异常就行了，spring security会自动跳到权限不足处理类
 *            （WebSecurityConfig 类中 配置文件上配的）
 *
 *
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccessDecisionManager.class);

    /**
     *  授权策略
     *
     * decide()方法在url请求时才会调用，服务器启动时不会执行这个方法
     *
     * @param configAttributes 装载了请求的url允许的角色数组 。这里是从MyInvocationSecurityMetadataSource
     *                         里的loadResourceDefine方法里的atts对象取出的角色数据赋予给了configAttributes对象
     * @param object url
     * @param authentication 装载了从数据库读出来的权限(角色) 数据。这里是从MyUserDetailService里的loadUserByUsername
     *                       方法里的grantedAuths对象的值传过来给 authentication 对象,简单点就是从spring的全局缓存SecurityContextHolder中拿到的，里面是用户的权限信息
     *
     * 注意： Authentication authentication 如果是前后端分离 则有跨域问题，跨域情况下
     *                       authentication 无法获取当前登陆人的身份认证(登陆成功后)，我尝试用token来效验权限
     *
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(CollectionUtils.isEmpty(configAttributes)) {
            LOGGER.info("无访问权限");
            throw new AccessDeniedException("无访问权限");
        }
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute configAttr = iterator.next();
            String needRole = configAttr.getAttribute();
            for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                //grantedAuthority 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
                //判断两个请求的url的权限和用户具有的权限是否相同，如相同，允许访问 权限就是那些以ROLE_为前缀的角色
                if(needRole.trim().equals(grantedAuthority.getAuthority().trim())) {
                    return;
                }
            }
        }
        //该url具有访问权限，但是当前登录用户没有匹配到URL对应的权限，则抛出无权限错误
        LOGGER.info("无权限访问");
        throw new AccessDeniedException("无权限访问");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

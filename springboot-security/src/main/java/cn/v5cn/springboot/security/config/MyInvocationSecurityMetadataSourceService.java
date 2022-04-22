package cn.v5cn.springboot.security.config;

import cn.v5cn.springboot.security.util.UrlMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 *
 * @FileName: MyInvocationSecurityMetadataSourceService
 * @Company:
 * @author
 * @Date      2018年05月11日
 * @version   1.0.0
 * @remark:   加载资源与权限的对应关系
 * @explain 实现FilterInvocationSecurityMetadataSource接口也是必须的。 首先，这里从数据库中获取信息。 其中loadResourceDefine方法不是必须的，
 *           这个只是加载所有的资源与权限的对应关系并缓存起来，避免每次获取权限都访问数据库（提高性能），然后getAttributes根据参数（被拦截url）返回权限集合。
 *           这种缓存的实现其实有一个缺点，因为loadResourceDefine方法是放在构造器上调用的，而这个类的实例化只在web服务器启动时调用一次，
 *           那就是说loadResourceDefine方法只会调用一次，
 *           如果资源和权限的对应关系在启动后发生了改变，那么缓存起来的权限数据就和实际授权数据不一致，那就会授权错误了。
 *           但如果资源和权限对应关系是不会改变的，这种方法性能会好很多。
 *           要想解决 权限数据的一致性 可以直接在getAttributes方法里面调用数据库操作获取权限数据，通过被拦截url获取数据库中的所有权限，
 *           封装成Collection<ConfigAttribute>返回就行了。（灵活、简单
 *
 *           器启动加载顺序：1：调用loadResourceDefine()方法  2：调用supports()方法   3：调用getAllConfigAttributes()方法
 *
 *
 */
@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInvocationSecurityMetadataSourceService.class);

    //存放资源配置对象
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    @Autowired
    private UrlMatcher urlMatcher;

    /**
     * 参数是要访问的url，返回这个url对于的所有权限（或角色）
     * 每次请求后台就会调用 得到请求所拥有的权限
     * 这个方法在url请求时才会调用，服务器启动时不会执行这个方法
     * getAttributes这个方法会根据你的请求路径去获取这个路径应该是有哪些权限才可以去访问。
     *
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // if (resourceMap == null){  //取消这段代码注释 情况下 每次服务启动后请求后台只有到数据库中取一次权限   如果注释掉这段代码则每次请求都会到数据库中取权限
        loadResourceDefine();  // 每次请求 都会去数据库查询权限  貌似很耗性能
        // }
        // object 是一个URL，被用户请求的url。
        String url = ((FilterInvocation) object).getRequestUrl();
        LOGGER.info("请求的URL: " + url);
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        //循环已经有的角色配置对象 进行url匹配
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resUrl = ite.next().trim();
            if(urlMatcher.pathMatchesUrl(resUrl,url)) { // 路径支持Ant风格的通配符 /spitters/**
                return resourceMap.get(resUrl);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        //要返回true  不然要报异常　　 SecurityMetadataSource does not support secure object class: class
        return true;
    }

    /**
     * 初始化资源 ,提取系统中的所有权限，加载所有url和权限（或角色）的对应关系，  web容器启动就会执行
     * 如果启动@PostConstruct 注解   则web容器启动就会执行
     */
    //@PostConstruct
    public void loadResourceDefine() {
        //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        resourceMap = new ConcurrentHashMap<>(20);
        //获取所有分配的角色
        /*List<SysRole> roleList = this.roleService.findByRoleModule();
        //容器启动时,获取全部系统菜单资源信息
        List<SysModuleVO> moduleList = this.moduleService.findByRoleModule();
        if (!CollectionUtils.isEmpty(roleList)){
            for (SysRole role : roleList){
                //授权标识
                String authorizedSigns = role.getAuthorizedSigns().trim();
                ConfigAttribute configAttributes = new SecurityConfig(authorizedSigns);
                for (SysModuleVO module : moduleList){
                    boolean flag = String.valueOf(role.getId()).equals(module.getAuthorizedSigns());
                    if(flag){
                        //请求url
                        String url =StringUtils.isNotBlank(module.getMenuUrl()) ? module.getMenuUrl().trim() : "";
                        // 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
                        if (resourceMap.containsKey(url)) {
                            Collection<ConfigAttribute> value = resourceMap.get(url);
                            value.add(configAttributes);
                            resourceMap.put(url, value);
                        } else {
                            Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
                            atts.add(configAttributes);
                            resourceMap.put(url, atts);
                        }
                    }
                }
            }
        }*/
    }
}

package cn.v5cn.cim.common.core.proxy;

import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.util.HttpClient;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author crossoverJie
 */
public class ProxyManager<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);

    private Class<T> clazz;

    private String url;

    private OkHttpClient okHttpClient;

    /**
     *
     * @param clazz Proxied interface
     * @param url server provider url
     * @param okHttpClient http client
     */
    public ProxyManager(Class<T> clazz, String url, OkHttpClient okHttpClient) {
        this.clazz = clazz;
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    /**
     * Get proxy instance of api.
     * @return
     */
    public T getInstance() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ProxyInvocation());
    }

    private class ProxyInvocation implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            JSONObject jsonObject = new JSONObject();
            String serverUrl = url + "/" + method.getName();

            if(args != null && args.length > 1) {
                throw new CIMException(StatusEnum.VALIDATION_FAIL);
            }

            if(method.getParameterTypes().length > 0) {
                Object para = args[0];
                Class<?> parameterType = method.getParameterTypes()[0];
                for(Field field : parameterType.getDeclaredFields()) {
                    field.setAccessible(true);
                    jsonObject.put(field.getName(), field.get(para));
                }
            }
            return HttpClient.call(okHttpClient, jsonObject.toString(), serverUrl);
        }
    }
/*

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = StringUtil.class.getMethod("isNullOrEmpty", String.class);
        Class<?> parameterType = HttpClient.class;//method.getParameterTypes()[0];
        System.out.println(parameterType.getName());
        for (Field field : parameterType.getDeclaredFields()) {
            System.out.println(field.getName());
        }
    }
*/

}

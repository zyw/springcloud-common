package cn.v5cn.springcloud.gateway.security;

import cn.v5cn.springcloud.gateway.exception.ErrorCode;
import cn.v5cn.springcloud.gateway.exception.ServerException;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class CustomRemoteTokenServices {

    private final Log logger = LogFactory.getLog(getClass());

    private LoadBalancerClient loadBalancerClient;

    private RestTemplate restTemplate;

    private String checkTokenEndpointUrl;

    private String clientId;

    private String clientSecret;

    private String tokenName = "token";

    private final String ERROR = "error";

    public CustomRemoteTokenServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl){
        this.checkTokenEndpointUrl = checkTokenEndpointUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public void loadAuthentication(String accessToken) {
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add(tokenName,accessToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(OAuth2AccessToken.AUTHORIZATION,getAuthorizationHeader(clientId,clientSecret));

        ServiceInstance serviceInstance = loadBalancerClient.choose("auth-server");
        if(serviceInstance == null) {
            throw new RuntimeException("Failed to choose an auth instance.");
        }
        Map<String, Object> postForMap = postForMap(serviceInstance.getUri().toString() + checkTokenEndpointUrl, formData, httpHeaders);

        if(postForMap.containsKey(ERROR)) {
            logger.debug("check_token returned error: " + postForMap.get(ERROR));
            Object status = postForMap.get("status");
            if(status != null && status.equals(HttpStatus.BAD_REQUEST.value())) {
                throw new ServerException(HttpStatus.BAD_REQUEST, new ErrorCode(400, "bad request!", "pls check your params!"));
            }

            HttpStatus code = (HttpStatus)postForMap.get(ERROR);
            if(code == HttpStatus.UNAUTHORIZED) {
                //TODO:sendErrorFilter findZuulException会查看FilterRuntimeException中zuulException的code和message
                throw new ServerException(HttpStatus.UNAUTHORIZED, new ErrorCode(401, "UNAUTHORIZED", "令牌不合法！"));
            }else {
                throw new ServerException(HttpStatus.FORBIDDEN, new ErrorCode(403, "not permitted!", "没有操作权限！"));
            }
        }

        Assert.state(postForMap.containsKey("client_id"), "Client id must be present in response from auth server");
    }

    private String getAuthorizationHeader(String clientId,String clientSecret) {
        String creds = String.format("%s:%s", clientId, clientSecret);

        return "Basic " + Base64.encodeBase64String(creds.getBytes(Charset.forName("UTF-8")));
    }

    private Map<String,Object> postForMap(String path, MultiValueMap<String,String> formData, HttpHeaders headers) {
        if(headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        Map<String,Object> map = Maps.newHashMap();

        try {
            map = restTemplate.exchange(path, HttpMethod.POST
                    ,new HttpEntity<>(formData,headers)
                    ,Map.class).getBody();
        } catch (HttpClientErrorException e) {
            logger.error("catch token exception when check token!", e);
            map.put(ERROR, e.getStatusCode());
        } catch (HttpServerErrorException e) {
            logger.error("catch no permission exception when check token!", e);
            map.put(ERROR, e.getStatusCode());
        }catch (Exception e) {
            logger.error("catch common exception when check token!", e);
        }

        return map;
    }
}

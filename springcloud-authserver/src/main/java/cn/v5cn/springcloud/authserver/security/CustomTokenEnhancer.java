package cn.v5cn.springcloud.authserver.security;

import com.google.common.collect.Maps;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.Serializable;
import java.util.Map;

public class CustomTokenEnhancer extends JwtAccessTokenConverter implements Serializable {

    private static int authenticateCodeExpiresTime = 10 * 60;

    private static final String TOKEN_SEG_USER_ID = "X-AOHO-UserId";
    private static final String TOKEN_SEG_CLIENT = "X-AOHO-ClientId";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
        authentication.getUserAuthentication().getPrincipal();

        Map<String,Object> info = Maps.newHashMap();
        info.put(TOKEN_SEG_USER_ID,userDetails.getUserId());

        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);

        OAuth2AccessToken enhance = super.enhance(customAccessToken, authentication);

        enhance.getAdditionalInformation().put(TOKEN_SEG_CLIENT,userDetails.getClientId());

        return enhance;
    }
}

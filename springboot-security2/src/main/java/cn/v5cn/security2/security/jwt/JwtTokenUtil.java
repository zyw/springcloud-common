package cn.v5cn.security2.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:19
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    //签名方式
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-expiration}")
    private Long tokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    //从jwt令牌检索用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //从jwt令牌检索到期日期
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 为了从令牌中检索任何信息，我们需要密钥
     * @param token 令牌
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //检查令牌是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成token
     * @param subject  用户名
     * @param claims
     * @return
     */
    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, tokenExpiration);
    }

    /**
     * 生成失效时间
     * @param expiration
     * @return
     */
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)   //一个map 可以资源存放东西进去
                .setSubject(subject) //  用户名写入标题
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration))  //过期时间
                //.setNotBefore(now)              //系统时间之前的token都是不可以被承认的
                .signWith(SIGNATURE_ALGORITHM, secret) //数字签名
                .compact();
    }


    /**
     * 刷新重新获取token
     * @param token 源token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getAllClaimsFromToken(token);
            refreshedToken = generateAccessToken(claims.getSubject(), claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


    //为用户生成令牌
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //创建令牌时：
    //1. 定义令牌的声明，如颁发者、过期、主题和ID
    //2. 使用HS512算法和密钥对JWT进行签名。
    //3. 根据JWS Compact序列化（https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1）将JWT压缩为URL安全字符串
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    //验证token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

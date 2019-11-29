package cn.celess.blog.util;

import cn.celess.blog.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 小海
 * @Date: 2019/11/16 11:26
 * @Description: JWT工具类
 */
@Component
public class JwtUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";

    /**
     * 5天(毫秒)
     */
    public static final long EXPIRATION_LONG_TIME = 432000000;

    /**
     * 两小时（毫秒）
     */
    public static final long EXPIRATION_SHORT_TIME = 7200000;
    /**
     * JWT 秘钥需自行设置不可泄露
     */
    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(User user, boolean isRemember) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USERNAME, user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(Instant.now().toEpochMilli() + (isRemember ? EXPIRATION_LONG_TIME : EXPIRATION_SHORT_TIME)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean validateToken(String token, User user) {
        String username = getUsernameFromToken(token);

        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    /**
     * 获取token是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 根据token获取username
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 解析JWT
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}

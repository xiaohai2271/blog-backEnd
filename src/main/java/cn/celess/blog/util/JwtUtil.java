package cn.celess.blog.util;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.User;
import cn.celess.blog.exception.MyException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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

    public String updateTokenDate(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(claims.getExpiration().getTime() + EXPIRATION_SHORT_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 获取token是否过期
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration == null || expiration.before(new Date());
    }

    /**
     * 根据token获取username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims == null ? null : claims.getSubject();
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims == null ? null : claims.getExpiration();
    }

    /**
     * 解析JWT
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("JWT令牌过期");
        } catch (UnsupportedJwtException e) {
            log.info("不支持的JWT令牌");
            throw new MyException(ResponseEnum.JWT_NOT_SUPPORT);
        } catch (MalformedJwtException e) {
            log.info("JWT令牌格式错误");
            throw new MyException(ResponseEnum.JWT_MALFORMED);
        } catch (SignatureException e) {
            log.info("JWT签名错误");
            throw new MyException(ResponseEnum.JWT_SIGNATURE);
        } catch (IllegalArgumentException e) {
            log.debug("JWT非法参数");
        }
        return claims;
    }

}

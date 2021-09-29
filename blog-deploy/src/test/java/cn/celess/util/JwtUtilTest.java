package cn.celess.util;

import cn.celess.BaseTest;
import cn.celess.common.entity.User;
import cn.celess.user.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JwtUtilTest extends BaseTest {

    @Autowired
    JwtUtil jwtUtil;
    @Value("${jwt.secret}")
    private String secret;

    @Test
    public void testGenerateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user, false);
        assertNotNull(s);
        String str = null;
        try {
            str = jwtUtil.generateToken(null, false);
        } catch (Exception e) {
            // ignore
        }
        assertNull(str);
    }

    @Test
    public void testIsTokenExpired() throws InterruptedException {
        String s = Jwts.builder()
                .setClaims(null)
                .setExpiration(new Date(Instant.now().toEpochMilli() + 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        Thread.sleep(1010);
        assertTrue(jwtUtil.isTokenExpired(s));
        assertFalse(jwtUtil.isTokenExpired(jwtUtil.generateToken(new User(), false)));
    }

    @Test
    public void testGetUsernameFromToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user, false);
        assertEquals(user.getEmail(), jwtUtil.getUsernameFromToken(s));
        user.setEmail("example@celess.cn");
        assertNotEquals(user.getEmail(), jwtUtil.getUsernameFromToken(s));
    }

    @Test
    public void testGetExpirationDateFromToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user, false);
        assertNotNull(jwtUtil.getExpirationDateFromToken(s));
    }

    @Test
    public void updateTokenDate() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user, false);
        Date before = jwtUtil.getExpirationDateFromToken(s);
        String s1 = jwtUtil.updateTokenDate(s);
        assertTrue(jwtUtil.getExpirationDateFromToken(s1).getTime() - jwtUtil.getExpirationDateFromToken(s).getTime() > 0);
    }
}
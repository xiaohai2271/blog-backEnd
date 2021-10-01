package cn.celess.user.util;

import cn.celess.common.entity.User;
import cn.celess.common.util.EnvironmentUtil;
import cn.celess.user.UserBaseTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JwtUtilTest extends UserBaseTest {

    @Test
    public void testGenerateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = JwtUtil.generateToken(user, false);
        assertNotNull(s);
        String str = null;
        try {
            str = JwtUtil.generateToken(null, false);
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
                .signWith(SignatureAlgorithm.HS512, EnvironmentUtil.getProperties("jwt.secret"))
                .compact();
        Thread.sleep(1010);
        assertTrue(JwtUtil.isTokenExpired(s));
        assertFalse(JwtUtil.isTokenExpired(JwtUtil.generateToken(new User(), false)));
    }

    @Test
    public void testGetUsernameFromToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = JwtUtil.generateToken(user, false);
        assertEquals(user.getEmail(), JwtUtil.getUsernameFromToken(s));
        user.setEmail("example@celess.cn");
        assertNotEquals(user.getEmail(), JwtUtil.getUsernameFromToken(s));
    }

    @Test
    public void testGetExpirationDateFromToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = JwtUtil.generateToken(user, false);
        assertNotNull(JwtUtil.getExpirationDateFromToken(s));
    }

    @Test
    public void updateTokenDate() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = JwtUtil.generateToken(user, false);
        Date before = JwtUtil.getExpirationDateFromToken(s);
        String s1 = JwtUtil.updateTokenDate(s);
        assertTrue(JwtUtil.getExpirationDateFromToken(s1).getTime() - JwtUtil.getExpirationDateFromToken(s).getTime() > 0);
    }
}
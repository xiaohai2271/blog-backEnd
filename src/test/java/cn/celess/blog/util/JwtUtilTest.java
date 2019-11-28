package cn.celess.blog.util;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class JwtUtilTest extends BaseTest {

    @Autowired
    JwtUtil jwtUtil;


    @Test
    public void generateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        String s = jwtUtil.generateToken(user, true);
        System.out.println(s);
        assertNotNull(s);
    }

    @Test
    public void validateToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        assertTrue(jwtUtil.validateToken(createToken(), user));
    }

    @Test
    public void isTokenExpired() {
        assertFalse(jwtUtil.isTokenExpired(createToken()));
    }

    @Test
    public void getUsernameFromToken() {
        assertEquals("a@celess.cn", jwtUtil.getUsernameFromToken(createToken()));
    }

    @Test
    public void getExpirationDateFromToken() {
        assertNotNull(jwtUtil.getExpirationDateFromToken(createToken()));
    }

    private String createToken() {
        User user = new User();
        user.setEmail("a@celess.cn");
        return jwtUtil.generateToken(user, true);
    }
}
package cn.celess.user.util;

import cn.celess.common.enmu.ResponseEnum;
import cn.celess.common.entity.User;
import cn.celess.common.exception.BlogResponseException;
import cn.celess.common.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/03/08 15:06
 */
@Component
public class RedisUserUtil {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    HttpServletRequest request;

    public User get() {
        User user = getWithOutExc();
        if (user == null) {
            throw new BlogResponseException(ResponseEnum.HAVE_NOT_LOG_IN);
        }
        return user;
    }

    @SneakyThrows
    public User getWithOutExc() {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        String email = jwtUtil.getUsernameFromToken(token);
        return new ObjectMapper().readValue(redisUtil.get(email + "-login"), User.class);
    }

    @SneakyThrows
    public User set(User user) {
        Long expire = redisUtil.getExpire(user.getEmail() + "-login");
        redisUtil.setEx(user.getEmail() + "-login", new ObjectMapper().writeValueAsString(user),
                expire > 0 ? expire : JwtUtil.EXPIRATION_SHORT_TIME, TimeUnit.MILLISECONDS);
        return user;
    }

    @SneakyThrows
    public User set(User user, boolean isRemember) {
        redisUtil.setEx(user.getEmail() + "-login", new ObjectMapper().writeValueAsString(user),
                isRemember ? JwtUtil.EXPIRATION_LONG_TIME : JwtUtil.EXPIRATION_SHORT_TIME, TimeUnit.MILLISECONDS);
        request.getSession().setAttribute("email", user.getEmail());
        return user;
    }
}

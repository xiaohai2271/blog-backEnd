package cn.celess.blog.util;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.User;
import cn.celess.blog.exception.MyException;
import net.sf.json.JSONObject;
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
            throw new MyException(ResponseEnum.HAVE_NOT_LOG_IN);
        }
        return user;
    }

    public User getWithOutExc() {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        }
        String email = jwtUtil.getUsernameFromToken(token);
        return (User) JSONObject.toBean(JSONObject.fromObject(redisUtil.get(email + "-login")), User.class);
    }

    public User set(User user) {
        Long expire = redisUtil.getExpire(user.getEmail() + "-login");
        redisUtil.setEx(user.getEmail() + "-login", JSONObject.fromObject(user).toString(),
                expire > 0 ? expire : JwtUtil.EXPIRATION_SHORT_TIME, TimeUnit.MILLISECONDS);
        return user;
    }

    public User set(User user, boolean isRemember) {
        redisUtil.setEx(user.getEmail() + "-login", JSONObject.fromObject(user).toString(),
                isRemember ? JwtUtil.EXPIRATION_LONG_TIME : JwtUtil.EXPIRATION_SHORT_TIME, TimeUnit.MILLISECONDS);
        request.getSession().setAttribute("email", user.getEmail());
        return user;
    }
}

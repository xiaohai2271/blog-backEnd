package cn.celess.blog.configuration.filter;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.JwtUtil;
import cn.celess.blog.util.RedisUtil;
import cn.celess.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 小海
 * @Date: 2019/11/16 11:21
 * @Description: 鉴权拦截器
 */
public class AuthenticationFilter implements HandlerInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static final String USER_PREFIX = "/user";
    private static final String ADMIN_PREFIX = "/admin";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        path = path.replaceAll("/+", "/");
        int indexOf = path.indexOf("/", 1);
        String rootPath = indexOf == -1 ? path : path.substring(0, indexOf);
        // 不需要鉴权的路径
        if (!USER_PREFIX.equals(rootPath.toLowerCase()) && !ADMIN_PREFIX.equals(rootPath.toLowerCase())) {
            return true;
        }

        String jwtStr = request.getHeader("Authorization");
        if (jwtStr == null || jwtStr.isEmpty()) {
            return writeResponse(ResponseEnum.HAVE_NOT_LOG_IN, response, request);
        }
        if (jwtUtil.isTokenExpired(jwtStr)) {
            return writeResponse(ResponseEnum.LOGIN_EXPIRED, response, request);
        }
        String email = jwtUtil.getUsernameFromToken(jwtStr);
        if (!redisUtil.hasKey(email + "-login") || jwtUtil.isTokenExpired(jwtStr)) {
            // 登陆过期
            return writeResponse(ResponseEnum.LOGIN_EXPIRED, response, request);
        }
        String role = userService.getUserRoleByEmail(email);
        if (role.equals(ROLE_ADMIN)) {
            // admin
            return true;
        }
        if (role.equals(ROLE_USER) && !rootPath.equals(ADMIN_PREFIX)) {
            // user  not admin page
            return true;
        }
        return writeResponse(ResponseEnum.PERMISSION_ERROR, response, request);
    }

    private boolean writeResponse(ResponseEnum e, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            logger.info("鉴权失败，[code:{},msg:{},path:{}]", e.getCode(), e.getMsg(), request.getRequestURI() + "?" + request.getQueryString());
            response.getWriter().println(JSONObject.fromObject(ResponseUtil.response(e, null)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

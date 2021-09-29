package cn.celess.configuration.filter;


import cn.celess.common.enmu.ResponseEnum;
import cn.celess.common.entity.Response;
import cn.celess.common.service.UserService;
import cn.celess.common.util.RedisUtil;
import cn.celess.user.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String USER_PREFIX = "/user";
    private static final String ADMIN_PREFIX = "/admin";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        path = path.replaceAll("/+", "/");
        int indexOf = path.indexOf("/", 1);
        String rootPath = indexOf == -1 ? path : path.substring(0, indexOf);
        String jwtStr = request.getHeader("Authorization");
        if (jwtStr != null && !jwtStr.isEmpty() && !jwtUtil.isTokenExpired(jwtStr)) {
            // 已登录 记录当前email
            request.getSession().setAttribute("email", jwtUtil.getUsernameFromToken(jwtStr));
        }
        // 不需要鉴权的路径
        if (!USER_PREFIX.equalsIgnoreCase(rootPath) && !ADMIN_PREFIX.equalsIgnoreCase(rootPath)) {
            return true;
        }

        if (jwtStr == null || jwtStr.isEmpty()) {
            return writeResponse(ResponseEnum.HAVE_NOT_LOG_IN, response, request);
        }
        if (jwtUtil.isTokenExpired(jwtStr)) {
            return writeResponse(ResponseEnum.LOGIN_EXPIRED, response, request);
        }
        String email = jwtUtil.getUsernameFromToken(jwtStr);
        if (jwtUtil.isTokenExpired(jwtStr)) {
            // 登陆过期
            return writeResponse(ResponseEnum.LOGIN_EXPIRED, response, request);
        }
        if (!redisUtil.hasKey(email + "-login")) {
            return writeResponse(ResponseEnum.LOGOUT, response, request);
        }
        String role = userService.getUserRoleByEmail(email);
        if (role.equals(ROLE_USER) || role.equals(ROLE_ADMIN)) {
            // 更新token
            String token = jwtUtil.updateTokenDate(jwtStr);
            response.setHeader("Authorization", token);
        }
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
            response.getWriter().println(new ObjectMapper().writeValueAsString(Response.response(e, null)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}

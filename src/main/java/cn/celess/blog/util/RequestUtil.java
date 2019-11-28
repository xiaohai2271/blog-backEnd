package cn.celess.blog.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 小海
 * @Date: 2019/10/18 15:44
 * @Description:
 */
public class RequestUtil {
    public static String getCompleteUrlAndMethod(HttpServletRequest request) {
        // like this : /articles?page=1&count=5:GET
        return request.getRequestURI() +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString()) +
                ":" + request.getMethod();
    }
}

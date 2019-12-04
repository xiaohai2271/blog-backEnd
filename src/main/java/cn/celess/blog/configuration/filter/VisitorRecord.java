package cn.celess.blog.configuration.filter;

import cn.celess.blog.util.RequestUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @Author: 小海
 * @Date: 2019/10/18 15:38
 * @Description: 记录访问情况
 */
@Configuration
public class VisitorRecord implements HandlerInterceptor {

    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        HashMap<String, Integer> visitDetail = (HashMap<String, Integer>) session.getAttribute("visitDetail");
        if (visitDetail == null) {
            return true;
        }
        // 获取访问次数
        Integer count = visitDetail.get(RequestUtil.getCompleteUrlAndMethod(request));
        // 自增
        count = count == null ? 1 : ++count;
        // 更新
        visitDetail.put(RequestUtil.getCompleteUrlAndMethod(request), count);
        session.setAttribute("ip", request.getRemoteAddr());
        return true;
    }
}

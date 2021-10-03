package cn.celess.configuration.filter;

import cn.celess.common.constant.ResponseEnum;
import cn.celess.common.entity.Response;
import cn.celess.common.util.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: 小海
 * @Date: 2019/10/18 13:46
 * @Description: 多次请求拦截器
 */
public class MultipleSubmitFilter implements HandlerInterceptor {
    private static final int WAIT_TIME = 200;// 多次提交中间的间隔

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long lastSubmitTime = (Long) request.getSession().getAttribute("lastSubmitTime");
        String completeUrl = (String) request.getSession().getAttribute("completeUrl&method");
        if (lastSubmitTime == null || completeUrl == null) {
            return true;
        }
        if (System.currentTimeMillis() - lastSubmitTime < WAIT_TIME && StringUtil.getCompleteUrlAndMethod(request).equals(completeUrl)) {
            // 请求参数和路径均相同 且请求时间间隔小于 WAIT_TIME
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Response result = new Response(ResponseEnum.FAILURE.getCode(), "重复请求", null);
            response.getWriter().println(result);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HttpSession session = request.getSession();
        session.setAttribute("lastSubmitTime", System.currentTimeMillis());
        session.setAttribute("completeUrl&method", StringUtil.getCompleteUrlAndMethod(request));
    }
}

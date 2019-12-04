package cn.celess.blog.configuration.listener;

import cn.celess.blog.util.RedisUserUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 * @Author: 小海
 * @Date: 2019/10/18 15:33
 * @Description: 监听session的情况
 */
@Log4j2
@WebListener
public class SessionListener implements HttpSessionListener {
    @Autowired
    RedisUserUtil redisUserUtil;
    @Autowired
    HttpServletRequest request;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // TODO : can move 'visit' api  to here
        se.getSession().setAttribute("visitDetail", new HashMap<String, Integer>());
        // 10s for debug
        // se.getSession().setMaxInactiveInterval(10);
        // log.info("新增一个Session[{}]", se.getSession().getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HashMap<String, Integer> visitDetail = (HashMap<String, Integer>) se.getSession().getAttribute("visitDetail");
        StringBuilder sb = new StringBuilder();
        sb.append("ip => ").append(se.getSession().getAttribute("ip"));
        if (visitDetail.size() == 0) {
            return;
        }
        sb.append("\t登录情况 => ");
        String email = (String) se.getSession().getAttribute("email");
        sb.append(email == null ? "游客访问" : email);
        visitDetail.forEach((s, integer) -> {
            sb.append("\n").append("Method:[").append(s.split(":")[1]).append("]\tTimes:[").append(integer).append("]\tPath:[").append(s.split(":")[0]).append("]");
        });
        log.info(sb.toString());
    }
}

package cn.celess.blog.configuration.listener;

import cn.celess.blog.entity.User;
import cn.celess.blog.util.RedisUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@WebListener
public class SessionListener implements HttpSessionListener {
    @Autowired
    RedisUserUtil redisUserUtil;
    @Autowired
    HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // TODO : can move 'visit' api  to here
        se.getSession().setAttribute("visitDetail", new HashMap<String, Integer>());
        // se.getSession().setMaxInactiveInterval(10);// 10s for debug
        logger.info("新增一个Session[{}]", se.getSession().getId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HashMap<String, Integer> visitDetail = (HashMap<String, Integer>) se.getSession().getAttribute("visitDetail");
        StringBuilder sb = new StringBuilder();
        sb.append("ip => ").append(se.getSession().getAttribute("ip"));
        User user = redisUserUtil.get();
        sb.append("\t登录情况 => ");
        sb.append(user == null ? "游客访问" : user.getEmail());
        visitDetail.forEach((s, integer) -> {
            sb.append("\n").append("Method:[").append(s.split(":")[1]).append("]\tTimes:[").append(integer).append("]\tPath:[").append(s.split(":")[0]).append("]");
        });
        logger.info(sb.toString());
    }
}

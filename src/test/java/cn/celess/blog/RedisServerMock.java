package cn.celess.blog;

import redis.embedded.RedisServer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author : xiaohai
 * @date : 2020/08/14 16:20
 */
@Component
public class RedisServerMock {

    private RedisServer redisServer;

    /**
     * 构造方法之后执行.
     *
     * @throws IOException e
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(6380);
        redisServer.start();
    }

    /**
     * 析构方法之后执行.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}

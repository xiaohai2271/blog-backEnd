package cn.celess.common.test;

import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author : xiaohai
 * @date : 2020/08/14 16:20
 */
public class RedisServerMock extends BaseTest {
    private static RedisServer redisServer;

    @PostConstruct
    public static void startRedis() {
        try {
            if (redisServer == null) {
                redisServer = new RedisServer(6380);
                redisServer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public static void stopRedis() {
        if (redisServer.isActive()) redisServer.stop();
    }
}
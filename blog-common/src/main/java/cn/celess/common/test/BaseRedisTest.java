package cn.celess.common.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author : xiaohai
 * @date : 2020/08/14 16:20
 */
@Configuration
@Slf4j
public class BaseRedisTest extends BaseTest {
    private static RedisServer redisServer;

    @PostConstruct
    public static void startRedis() {
        try {
            if (redisServer == null) {
                redisServer = new RedisServer(6380);
                redisServer.start();
                log.info("start Redis success");
            }
        } catch (IOException e) {
            log.error("start Redis failed");
            e.printStackTrace();
        }
    }

    @PreDestroy()
    public static void stopRedis() {
        if (redisServer.isActive()) {
            redisServer.stop();
            log.info("stop Redis success");
        }
    }
}
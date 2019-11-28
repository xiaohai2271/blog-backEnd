package cn.celess.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("cn.celess.blog.mapper")
public class BlogApplication {
    public static final Logger logger = LoggerFactory.getLogger(BlogApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        logger.info("启动完成！");
    }
}

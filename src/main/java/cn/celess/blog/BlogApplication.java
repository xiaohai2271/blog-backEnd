package cn.celess.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zheng
 */
@SpringBootApplication
@EnableAsync
@MapperScan("cn.celess.blog.mapper")
public class BlogApplication {
    public static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(BlogApplication.class, args);
    }
}

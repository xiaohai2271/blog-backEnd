package cn.celess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "cn.celess.common.test.BaseRedisTest")})
public class BlogApplication {
    public static final Logger logger = LoggerFactory.getLogger(BlogApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(BlogApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
        logger.info("启动完成！");
    }
}

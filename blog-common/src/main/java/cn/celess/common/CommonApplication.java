package cn.celess.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.SpringVersion;


@SpringBootApplication(
        scanBasePackageClasses = {
                CommonApplication.class
        }
)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "cn.celess.common.test.BaseRedisTest")})
@MapperScan("cn.celess.common.mapper")
public class CommonApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CommonApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

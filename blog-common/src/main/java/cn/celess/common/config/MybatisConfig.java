package cn.celess.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.celess.common.mapper")
public class MybatisConfig {
}

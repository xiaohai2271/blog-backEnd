package cn.celess.common.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.celess.common.mapper")
@Slf4j
public class MybatisConfig {

//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        log.info("配置Mybatis的配置项");
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        // 此处省略部分代码
//        bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
//        bean.setTypeAliasesPackage("cn.celess.common.entity");
//        return bean.getObject();
//    }
}

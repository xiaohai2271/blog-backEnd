package cn.celess.configuration;

import cn.celess.common.util.EnvironmentUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : xiaohai
 * @date : 2019/03/28 14:26
 */
@Configuration
public class DruidConfig {

    @Bean
    public DruidDataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(EnvironmentUtil.getProperties("spring.datasource.driver-class-name"));
        // 数据库基本信息
        dataSource.setUrl(EnvironmentUtil.getProperties("spring.datasource.url"));
        dataSource.setUsername(EnvironmentUtil.getProperties("spring.datasource.username"));
        dataSource.setPassword(EnvironmentUtil.getProperties("spring.datasource.password"));

        // 数据库连接池配置
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(100);
        return dataSource;
    }
}

package cn.celess.blog.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author : xiaohai
 * @date : 2019/03/28 14:26
 */
@Slf4j
@Configuration
public class DruidConfig {

    @Autowired
    Environment env;

    public static final String DB_CONFIG_PATH = System.getProperty("user.home") + "/blog/application.properties";
    public static final String DB_CONFIG_URL_PREFIX = "spring.datasource.url";
    public static final String DB_CONFIG_USERNAME_PREFIX = "spring.datasource.username";
    public static final String DB_CONFIG_PASSWORD_PREFIX = "spring.datasource.password";
    public static final String DB_CONFIG_DRIVER_CLASS_NAME_PREFIX = "spring.datasource.driver-class-name";

    @Bean
    public DruidDataSource initDataSource() throws IOException {
        DruidDataSource dataSource;
        File file = new File(DB_CONFIG_PATH);
        if (file.exists()) {
            log.debug("从文件中获取数据库配置");
            dataSource = readConfigFromFile(file);
        } else {
            log.debug("默认数据库配置");
            dataSource = defaultDruidSource();
        }
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(100);
        return dataSource;
    }

    private DruidDataSource readConfigFromFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        String url = properties.getProperty(DB_CONFIG_URL_PREFIX, null);
        String username = properties.getProperty(DB_CONFIG_USERNAME_PREFIX, null);
        String password = properties.getProperty(DB_CONFIG_PASSWORD_PREFIX, null);
        String className = properties.getProperty(DB_CONFIG_DRIVER_CLASS_NAME_PREFIX, null);
        if (url == null || username == null || password == null || className == null) {
            return defaultDruidSource();
        }
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(className);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }


    private DruidDataSource defaultDruidSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX));
        dataSource.setUrl(env.getProperty(DruidConfig.DB_CONFIG_URL_PREFIX));
        dataSource.setUsername(env.getProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX));
        dataSource.setPassword(env.getProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX));
        return dataSource;
    }
}

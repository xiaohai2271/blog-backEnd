package cn.celess.common.config;

import cn.celess.common.util.EnvironmentUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

@Component
public class CommonEnvPostProcessor implements EnvironmentPostProcessor, ApplicationListener<ApplicationEvent>, Ordered {
    public static final DeferredLog log = new DeferredLog();

    private static final String CONFIG_PATH = "/config/blog.properties";
    private static final String SOURCE_NAME = "localize";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication) {
        configurableEnvironment.getPropertySources().forEach(propertySource -> {
            if (propertySource.getName().startsWith("applicationConfig") && propertySource instanceof OriginTrackedMapPropertySource) {
                EnvironmentUtil.addProperties((Map<?, ?>) propertySource.getSource());
            }
        });

        log.info("加载本地配置文件");
        //获取环境变量
        String homeEnv = EnvironmentUtil.getEnv("BLOG_HOME", EnvironmentUtil.getEnv("USERPROFILE"));
        String configPath = (homeEnv + CONFIG_PATH).replaceAll("[\\|/]+", Matcher.quoteReplacement(File.separator));
        try (InputStream input = new FileInputStream(configPath)) {
            Properties properties = new Properties();
            properties.load(input);
            PropertiesPropertySource propertySource = new PropertiesPropertySource(SOURCE_NAME, properties);
            configurableEnvironment.getPropertySources().addLast(propertySource);
            log.info("Load the configuration file under the environment variable,end.");
            EnvironmentUtil.addProperties(properties);
        } catch (Exception e) {
            log.info("加载本地[" + configPath + "]的配置文件失败");
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.replayTo(CommonEnvPostProcessor.class);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

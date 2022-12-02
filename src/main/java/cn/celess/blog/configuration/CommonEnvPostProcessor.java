package cn.celess.blog.configuration;

import cn.celess.blog.util.EnvironmentUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * <p>date: 2022/12/02</P>
 * <p>desc: </p>
 * <p>mail: a@celess.cn</p>
 *
 * @author 禾几海
 */

@Component
public class CommonEnvPostProcessor implements EnvironmentPostProcessor, ApplicationListener<ApplicationEvent>, Ordered {
    public static final DeferredLog log = new DeferredLog();

    private static final String CONFIG_PATH = "/HBlog/config/blog.properties";
    private static final String SOURCE_NAME = "localize";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication) {

        EnvironmentUtil.setEnvironment(configurableEnvironment);

        log.info("加载本地配置文件--");
        //获取环境变量
        String homeEnv = EnvironmentUtil.getEnv("BLOG_HOME", EnvironmentUtil.getEnv("USERPROFILE"));
        String configPath = (homeEnv + CONFIG_PATH).replaceAll("[\\|/]+", Matcher.quoteReplacement(File.separator));
        try (InputStream input = Files.newInputStream(Paths.get(configPath))) {
            Properties properties = new Properties();
            properties.load(input);
            PropertiesPropertySource propertySource = new PropertiesPropertySource(SOURCE_NAME, properties);
            configurableEnvironment.getPropertySources().addLast(propertySource);
            log.info("成功加载本地配置文件:)");
        } catch (Exception e) {
            log.info("加载本地[" + configPath + "]的配置文件失败:(");
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationEvent event) {
        log.replayTo(CommonEnvPostProcessor.class);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

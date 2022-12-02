package cn.celess.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * <p>date: 2022/12/02</P>
 * <p>desc: </p>
 * <p>mail: a@celess.cn</p>
 *
 * @author 禾几海
 */

@Slf4j
public class EnvironmentUtil {

    private static Environment environment;

    public static String getEnv(String name) {
        String value = System.getenv(name);
        if (StringUtils.isBlank(value)) {
            log.error("没有找到环境变量:" + name);
        }
        return value;
    }

    public static String getEnv(String name, String defaultValue) {
        String env = getEnv(name);
        if (env == null) {
            return defaultValue;
        }
        return env;
    }

    public static String getProperties(String key) {
        String value = environment.getProperty(key);
        if (StringUtils.isBlank(value)) {
            log.error("没有找到配置项: {}", key);
        }
        return value;
    }

    public static String getProperties(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static void setEnvironment(Environment environment) {
        EnvironmentUtil.environment = environment;
    }
}

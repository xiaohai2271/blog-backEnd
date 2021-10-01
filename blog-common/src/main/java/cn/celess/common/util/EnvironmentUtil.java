package cn.celess.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Properties;

@Slf4j
public class EnvironmentUtil {

    private static final Properties properties = new Properties();


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
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            log.error("没有找到配置项: {}", key);
        }
        return value;
    }

    public static String getProperties(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }


    public static void addProperties(Map<?, ?> map) {
        properties.putAll(map);
    }
}

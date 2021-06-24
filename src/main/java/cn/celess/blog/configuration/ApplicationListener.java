package cn.celess.blog.configuration;

import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.entity.Config;
import cn.celess.blog.mapper.ConfigMapper;
import cn.celess.blog.service.fileserviceimpl.LocalFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : xiaohai
 * @date : 2020/10/16 16:00
 * @desc :
 */
@Component
@Slf4j
public class ApplicationListener implements ApplicationRunner {

    @Autowired
    ConfigMapper configMapper;
    @Autowired
    Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("博客启动！");
        // 设置初始值
        setProperty(ConfigKeyEnum.FILE_QINIU_SECRET_KEY);
        setProperty(ConfigKeyEnum.FILE_QINIU_ACCESS_KEY);
        setProperty(ConfigKeyEnum.FILE_QINIU_BUCKET);
        setProperty(ConfigKeyEnum.BLOG_FILE_PATH);

        List<Config> configurations = configMapper.getConfigurations()
                .stream()
                .filter(config -> config.getValue() != null && !"".equals(config.getValue()))
                .collect(Collectors.toList());
        configurations.forEach(config -> System.setProperty(config.getName(), config.getValue()));
        log.debug("注入配置成功 {}", configurations.stream().map(Config::getName).collect(Collectors.toList()));
        File path = new File(LocalFileServiceImpl.getPath(System.getProperty(ConfigKeyEnum.BLOG_FILE_PATH.getKey())));
        if (!path.exists() && !path.mkdirs()) {
            throw new IllegalAccessException("创建数据目录失败==>" + path.getAbsolutePath());
        }
    }

    private void setProperty(ConfigKeyEnum e) {
        String property = env.getProperty(e.getKey());
        if (property != null) {
            System.setProperty(e.getKey(), property);
        }
    }
}

package cn.celess.blog.configuration;

import cn.celess.blog.entity.Config;
import cn.celess.blog.mapper.ConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("博客启动！");
        List<Config> configurations = configMapper.getConfigurations();
        configurations.forEach(config -> System.setProperty(config.getName(), config.getValue()));
        log.debug("注入配置成功 {}", configurations.stream().map(Config::getName).collect(Collectors.toList()));
    }
}

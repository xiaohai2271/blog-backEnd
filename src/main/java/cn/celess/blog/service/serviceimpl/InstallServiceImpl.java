package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.BlogApplication;
import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Config;
import cn.celess.blog.entity.InstallParam;
import cn.celess.blog.entity.User;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ConfigMapper;
import cn.celess.blog.mapper.UserMapper;
import cn.celess.blog.service.InstallService;
import cn.celess.blog.service.fileserviceimpl.LocalFileServiceImpl;
import cn.celess.blog.util.MD5Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author : xiaohai
 * @date : 2020/10/23 16:23
 * @desc :
 */
@Slf4j
@Service
public class InstallServiceImpl implements InstallService {

    public static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String H2_DRIVER_CLASS_NAME = "org.h2.Driver";

    @Autowired
    ConfigMapper configMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Map<String, Object> isInstall() {
        Config installed = configMapper.getConfiguration(ConfigKeyEnum.BLOG_INSTALLED.getKey());
        Boolean isInstall = Boolean.valueOf(installed.getValue());
        Map<String, Object> result = new HashMap<>(3);
        result.put("is_install", isInstall);
        if (isInstall) {
            Map<String, String> configMap = configMapper.getConfigurations()
                    .stream()
                    .filter(config -> config.getValue() != null)
                    .collect(Collectors.toMap(Config::getName, Config::getValue));
            result.put(ConfigKeyEnum.FILE_TYPE.getKey(), configMap.get(ConfigKeyEnum.FILE_TYPE.getKey()));
            result.put(ConfigKeyEnum.DB_TYPE.getKey(), configMap.get(ConfigKeyEnum.DB_TYPE.getKey()));
        }
        return result;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> install(@NotNull InstallParam installParam) {
        User user = new User(installParam.getEmail(), MD5Util.getMD5(installParam.getPassword()));
        userMapper.addUser(user);
        userMapper.setUserRole(user.getId(), "admin");


        Config install = configMapper.getConfiguration(ConfigKeyEnum.BLOG_INSTALLED.getKey());
        boolean isInstall = Boolean.parseBoolean(install.getValue());
        if (isInstall) {
            throw new MyException(ResponseEnum.FAILURE, "已经安装过了");
        }

        Config config = new Config(ConfigKeyEnum.DB_TYPE);
        config.setValue(installParam.getDbType());
        Properties properties = new Properties();
        if ("h2".equals(installParam.getDbType())) {
            properties.setProperty(ConfigKeyEnum.DB_DRIVER_CLASS_NAME.getKey(), H2_DRIVER_CLASS_NAME);
        } else {
            properties.setProperty(ConfigKeyEnum.DB_DRIVER_CLASS_NAME.getKey(), MYSQL_DRIVER_CLASS_NAME);
        }
        properties.setProperty(ConfigKeyEnum.DB_URL.getKey(), installParam.getDbUrl());
        properties.setProperty(ConfigKeyEnum.DB_USERNAME.getKey(), installParam.getDbUsername());
        properties.setProperty(ConfigKeyEnum.DB_PASSWORD.getKey(), installParam.getDbPassword());

        Config configuration = configMapper.getConfiguration(ConfigKeyEnum.BLOG_DB_PATH.getKey());

        File file = new File(LocalFileServiceImpl.getPath(configuration.getValue()));
        if (!file.exists() && file.createNewFile()) {
            log.info("创建数据库配置文件: {}", file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file);
        properties.load(fis);

        configMapper.addConfiguration(config);
        properties.store(fos, "DB CONFIG");

        install.setValue(Boolean.valueOf(true).toString());
        configMapper.updateConfiguration(install);

        log.info("重启...");

        // 重启
        BlogApplication.restart();
        return null;
    }
}

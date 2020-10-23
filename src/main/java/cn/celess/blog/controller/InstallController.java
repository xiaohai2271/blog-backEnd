package cn.celess.blog.controller;

import cn.celess.blog.BlogApplication;
import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Config;
import cn.celess.blog.entity.InstallParam;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.User;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ConfigMapper;
import cn.celess.blog.mapper.UserMapper;
import cn.celess.blog.service.InstallService;
import cn.celess.blog.service.fileserviceimpl.LocalFileServiceImpl;
import cn.celess.blog.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author : xiaohai
 * @date : 2020/10/18 15:36
 * @desc :
 */
@Slf4j
@Controller
public class InstallController {

    @Autowired
    InstallService installService;
    @Autowired
    ConfigMapper configMapper;


    @GetMapping("/is_install")
    @ResponseBody
    public Response<Map<String, Object>> isInstall() {
        Map<String, Object> install = installService.isInstall();
        return Response.success(install);
    }


    @PostMapping("/install")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Response install(@RequestBody InstallParam installParam) {
        return Response.success(installParam);
    }


    @GetMapping("/install")
    public String install() {
        Config configuration = configMapper.getConfiguration(ConfigKeyEnum.BLOG_INSTALLED.getKey());
        if (Boolean.parseBoolean(configuration.getValue())) {
            return "index.html";
        }
        log.info("博客第一次运行，还未安装");
        return "install.html";
    }
}

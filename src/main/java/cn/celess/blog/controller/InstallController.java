package cn.celess.blog.controller;

import cn.celess.blog.BlogApplication;
import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Config;
import cn.celess.blog.entity.InstallParam;
import cn.celess.blog.entity.Response;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ConfigMapper;
import cn.celess.blog.service.InstallService;
import cn.celess.blog.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author : xiaohai
 * @date : 2020/10/18 15:36
 * @desc :
 */
@Slf4j
@Controller
@Validated
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
    public Response<Object> install(@Valid @RequestBody InstallParam installParam) {
        if (!RegexUtil.emailMatch(installParam.getEmail())) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }
        if (!RegexUtil.pwdMatch(installParam.getPassword())) {
            throw new MyException(ResponseEnum.PARAMETERS_PWD_ERROR);
        }
        return Response.success(installService.install(installParam));
    }


    @GetMapping("/default_config")
    @ResponseBody
    public Response<String> defaultConfig() {
        return null;
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

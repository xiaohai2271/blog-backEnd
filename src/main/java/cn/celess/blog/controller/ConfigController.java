package cn.celess.blog.controller;

import cn.celess.blog.entity.Config;
import cn.celess.blog.entity.Response;
import cn.celess.blog.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2020/10/16 20:56
 * @desc : 配置管理接口
 */
@RestController
public class ConfigController {
    @Autowired
    ConfigMapper configMapper;

    @GetMapping("/admin/config")
    public Response<List<Config>> getConfiguration() {
        return Response.success(configMapper.getConfigurations());
    }

    @PutMapping("/admin/config")
    public Response<List<Config>> updateConfiguration(@RequestBody List<Config> configs) {
        configs.forEach(config -> configMapper.updateConfiguration(config));
        configs.forEach(config -> System.setProperty(config.getName(), config.getValue()));
        return Response.success(configMapper.getConfigurations());
    }

    @PostMapping("/admin/config")
    public Response<List<Config>> addConfiguration(@RequestBody List<Config> configs) {
        configs.forEach(config -> configMapper.addConfiguration(config));
        configs.forEach(config -> System.setProperty(config.getName(), config.getValue()));
        return Response.success(configMapper.getConfigurations());
    }
}

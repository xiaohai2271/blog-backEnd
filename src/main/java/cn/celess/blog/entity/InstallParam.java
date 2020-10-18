package cn.celess.blog.entity;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2020/10/18 14:52
 * @desc :
 */
@Data
public class InstallParam {
    private String dbType;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
}

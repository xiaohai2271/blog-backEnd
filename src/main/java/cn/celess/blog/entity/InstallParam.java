package cn.celess.blog.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : xiaohai
 * @date : 2020/10/18 14:52
 * @desc :
 */
@Data
public class InstallParam {
    @NotBlank(message = "数据库类型不可为空")
    private String dbType;

    @NotBlank(message = "数据库链接不可为空")
    private String dbUrl;

    @NotBlank(message = "数据库用户名不可为空")
    private String dbUsername;

    @NotBlank(message = "数据库密码不可为空")
    private String dbPassword;


    /**
     * user 相关
     */
    @NotBlank(message = "用户邮箱地址不可为空")
    private String email;

    @NotBlank(message = "用户密码不可为空")
    private String password;
}

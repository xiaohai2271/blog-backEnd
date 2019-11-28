package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/06/01 22:47
 */
@Data
public class LoginReq {
    private String email;
    private String password;
    /**
     * isRememberMe默认为false
     */
    private Boolean isRememberMe = false;

}


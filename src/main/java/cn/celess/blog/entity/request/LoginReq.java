package cn.celess.blog.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaohai
 * @date : 2019/06/01 22:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
    private String email;
    private String password;
    /**
     * isRememberMe默认为false
     */
    private Boolean isRememberMe = false;

}


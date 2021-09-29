package cn.celess.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/06/01 22:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq implements Serializable {
    private String email;
    private String password;
    /**
     * isRememberMe默认为false
     */
    private Boolean isRememberMe = false;

}


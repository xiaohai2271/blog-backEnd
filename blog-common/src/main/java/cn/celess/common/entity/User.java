package cn.celess.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/03/28 14:52
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    @JsonIgnore
    private String pwd;

    /**
     * 昵称
     */
    private String displayName;

    private Boolean emailStatus = false;

    /**
     * 头像地址
     */
    private String avatarImgUrl;

    private String desc;

    private Date recentlyLandedDate;

    private String role = "user";

    private int status;

    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }
}

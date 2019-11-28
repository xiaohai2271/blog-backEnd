package cn.celess.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/03/28 14:52
 */
@Data
public class User {
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户唯一标识码
     */
    @JsonIgnore
    private String uid;

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

    /**
     * 随机码 用户验证邮箱/找回密码
     * 暂时废弃这一字段
     */
    private String emailVerifyId;

    private String role = "user";

    public User() {
    }

}

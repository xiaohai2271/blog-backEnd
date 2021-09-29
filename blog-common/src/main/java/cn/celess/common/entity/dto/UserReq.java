package cn.celess.common.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 小海
 * @Date： 2019/09/06 13:33
 * @Description：
 */
@Data
public class UserReq implements Serializable {
    private Long id;

    private String email;

    private String pwd;

    private String displayName;

    private Boolean emailStatus;

    private String desc;

    private String role;

}

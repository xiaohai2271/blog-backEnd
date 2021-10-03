package cn.celess.common.constant;

import lombok.Getter;

/**
 * @Author: 小海
 * @Date: 2020-05-24 16:31
 * @Desc:
 */
@Getter
public enum RoleEnum {
    // admin 权限
    ADMIN_ROLE("admin"),
    // user 权限
    USER_ROLE("user");


    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }
}

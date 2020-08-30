package cn.celess.blog.enmu;

import java.util.Arrays;

/**
 * @Author: 小海
 * @Date: 2020-05-22 21:32
 * @Desc:
 */
public enum UserAccountStatusEnum {

    /**
     * 账户正常
     */
    NORMAL(0, "正常"),
    /**
     * 账户被锁定
     */
    LOCKED(1, "锁定"),
    /**
     * 账户被删除
     */
    DELETED(2, "已删除");


    private final int code;
    private final String desc;

    UserAccountStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserAccountStatusEnum get(int code) {
        for (UserAccountStatusEnum value : UserAccountStatusEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}

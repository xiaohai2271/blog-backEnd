package cn.celess.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

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

    @JsonCreator
    public static UserAccountStatusEnum get(Map<String, Object> map) {
        for (UserAccountStatusEnum value : UserAccountStatusEnum.values()) {
            if (value.code == (int) map.get("code") && value.desc.equals(map.get("desc"))) {
                return value;
            }
        }
        return null;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code);
        map.put("desc", desc);
        return map;
    }
}

package cn.celess.common.enmu;

import lombok.Getter;

/**
 * @Author: 小海
 * @Date: 2020-05-25 08:58
 * @Desc:
 */
@Getter
public enum CommentStatusEnum {
    // 正常
    NORMAL(0, "正常"),
    DELETED(3, "已删除");

    private final int code;
    private final String msg;

    CommentStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

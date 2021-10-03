package cn.celess.common.exception;

import cn.celess.common.constant.ResponseEnum;
import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/03/28 16:56
 */
@Data
public class BlogResponseException extends RuntimeException {
    private int code;
    private Object result;

    public BlogResponseException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BlogResponseException(ResponseEnum e) {
        super(e.getMsg());
        this.code = e.getCode();
    }

    public BlogResponseException(ResponseEnum e, Object result) {
        super(e.getMsg());
        this.code = e.getCode();
        this.result = result;
    }

    public BlogResponseException(ResponseEnum e, String msg) {
        super(msg + e.getMsg());
        this.code = e.getCode();
    }

    public BlogResponseException(ResponseEnum e, String msg, Object result) {
        super(e.getMsg());
        this.code = e.getCode();
        this.result = result;
    }
}

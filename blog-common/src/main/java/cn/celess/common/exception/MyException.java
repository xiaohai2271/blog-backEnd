package cn.celess.common.exception;

import cn.celess.common.enmu.ResponseEnum;
import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/03/28 16:56
 */
@Data
public class MyException extends RuntimeException {
    private int code;
    private Object result;

    public MyException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public MyException(ResponseEnum e) {
        super(e.getMsg());
        this.code = e.getCode();
    }

    public MyException(ResponseEnum e, Object result) {
        super(e.getMsg());
        this.code = e.getCode();
        this.result = result;
    }

    public MyException(ResponseEnum e, String msg) {
        super(msg + e.getMsg());
        this.code = e.getCode();
    }

    public MyException(ResponseEnum e, String msg, Object result) {
        super(e.getMsg());
        this.code = e.getCode();
        this.result = result;
    }
}

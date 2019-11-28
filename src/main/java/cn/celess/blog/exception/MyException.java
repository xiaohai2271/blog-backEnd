package cn.celess.blog.exception;

import cn.celess.blog.enmu.ResponseEnum;

/**
 * @author : xiaohai
 * @date : 2019/03/28 16:56
 */
public class MyException extends RuntimeException {
    private int code;

    public MyException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public MyException(ResponseEnum e) {
        super(e.getMsg());
        this.code = e.getCode();
    }

    public MyException(ResponseEnum e, String msg) {
        super(msg + e.getMsg());
        this.code = e.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

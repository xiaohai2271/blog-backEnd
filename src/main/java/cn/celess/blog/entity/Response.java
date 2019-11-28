package cn.celess.blog.entity;

import lombok.Data;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:24
 */
@Data
public class Response implements Serializable {
    private int code;
    private String msg;
    private Object result;
    private long date;

    public Response() {
    }

    public Response(int code, String msg, Object result, long date) {
        this.code = code;
        this.msg = msg;
        this.result = result;
        this.date = date;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}

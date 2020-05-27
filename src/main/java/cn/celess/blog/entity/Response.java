package cn.celess.blog.entity;

import cn.celess.blog.enmu.ResponseEnum;
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

    public Response() {
    }

    public Response(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    /**
     * 成功相应
     *
     * @param result 结果
     * @return Response
     */
    public static Response success(Object result) {
        return new Response(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), result);
    }

    /**
     * 失败的响应
     *
     * @param result 结果
     * @return Response
     */
    public static Response failure(String result) {
        return new Response(ResponseEnum.FAILURE.getCode(), ResponseEnum.FAILURE.getMsg(), result);
    }

    /**
     * 其他的响应
     *
     * @param r      枚举常量
     * @param result 结果
     * @return Response
     */
    public static Response response(ResponseEnum r, String result) {
        return new Response(r.getCode(), r.getMsg(), result);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}

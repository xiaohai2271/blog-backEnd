package cn.celess.blog.entity;

import cn.celess.blog.enmu.ResponseEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:24
 */
@Data
public class Response<T> implements Serializable {
    private int code;
    private String msg;
    private T result;

    public Response() {
    }

    public Response(int code, String msg, T result) {
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
    public static <T> Response<T> success(T result) {
        return new Response<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), result);
    }

    /**
     * 失败的响应
     *
     * @param result 结果
     * @return Response
     */
    public static Response<String> failure(String result) {
        return new Response<String>(ResponseEnum.FAILURE.getCode(), ResponseEnum.FAILURE.getMsg(), result);
    }

    /**
     * 其他的响应
     *
     * @param r      枚举常量
     * @param result 结果
     * @return Response
     */
    public static <T> Response<T> response(ResponseEnum r, T result) {
        return new Response<T>(r.getCode(), r.getMsg(), result);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writeValueAsString(this);
    }
}

package cn.celess.blog.util;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Response;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:32
 */
@ResponseBody
public class ResponseUtil {

    /**
     * 成功相应
     *
     * @param result 结果
     * @return
     */
    public static Response success(Object result) {
        Response response = new Response();
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMsg(ResponseEnum.SUCCESS.getMsg());
        response.setDate(System.currentTimeMillis());
        response.setResult(result);
        return response;
    }

    /**
     * 失败的响应
     *
     * @param result 结果
     * @return
     */
    public static Response failure(String result) {
        Response response = new Response();
        response.setCode(ResponseEnum.FAILURE.getCode());
        response.setMsg(ResponseEnum.FAILURE.getMsg());
        response.setDate(System.currentTimeMillis());
        response.setResult(result);
        return response;
    }

    /**
     * 其他的响应
     *
     * @param r      枚举常量
     * @param result 结果
     * @return
     */
    public static Response response(ResponseEnum r, String result) {
        Response response = new Response();
        response.setCode(r.getCode());
        response.setMsg(r.getMsg());
        response.setDate(System.currentTimeMillis());
        response.setResult(result);
        return response;
    }
}

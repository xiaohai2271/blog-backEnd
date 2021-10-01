package cn.celess.common.util;

import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/03/28 17:21
 */
public class StringUtil {
    /**
     * 从html中提取纯文本
     *
     * @param html html string
     * @return 纯文本
     */
    public static String getString(String html) {
        //剔出<html>的标签
        String txtcontent = html.replaceAll("</?[^>]+>", "");
        //去除字符串中的空格,回车,换行符,制表符
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        return txtcontent;
    }

    /**
     * 生成MD5
     *
     * @param str 源数据
     * @return md5 内容
     */
    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String getCompleteUrlAndMethod(HttpServletRequest request) {
        // like this : GET:/articles?page=1&count=5
        return request.getMethod() + ":" + request.getRequestURI() +
                (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}


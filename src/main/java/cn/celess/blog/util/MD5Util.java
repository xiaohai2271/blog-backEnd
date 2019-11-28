package cn.celess.blog.util;

import org.springframework.util.DigestUtils;

/**
 * @author : xiaohai
 * @date : 2019/03/30 18:56
 */
public class MD5Util {
    public static String getMD5(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}

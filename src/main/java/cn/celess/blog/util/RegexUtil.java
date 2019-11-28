package cn.celess.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:04
 */
public class RegexUtil {
    /**
     * 网址匹配
     *
     * @param url
     * @return
     */
    public static boolean urlMatch(String url) {
        if (url == null || url.replaceAll(" ", "").isEmpty()) {
            return false;
        }
        //正则 （http(s)://www.celess/xxxx,www.celess.cn/xxx）
        String pattern = "^(http://|https://|)([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        return match(url, pattern);
    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean emailMatch(String email) {
        if (email == null || email.replaceAll(" ", "").isEmpty()) {
            return false;
        }
        //正则
        String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        return match(email, pattern);
    }

    /**
     * 手机号匹配
     *
     * @param phone
     * @return
     */
    public static boolean phoneMatch(String phone) {
        if (phone == null || phone.replaceAll(" ", "").isEmpty()) {
            return false;
        }
        //正则
        String pattern = "^([1][3,4,5,6,7,8,9])\\d{9}$";
        return match(phone, pattern);
    }

    /**
     * 密码正则
     * 最短6位，最长16位 {6,16}
     * 可以包含小写大母 [a-z] 和大写字母 [A-Z]
     * 可以包含数字 [0-9]
     * 可以包含下划线 [ _ ] 和减号 [ - ]
     *
     * @param pwd
     * @return
     */
    public static boolean pwdMatch(String pwd) {
        if (pwd == null || pwd.replaceAll(" ", "").isEmpty()) {
            return false;
        }
        //正则
        String pattern = "^[\\w_-]{6,16}$";
        return match(pwd, pattern);
    }

    private static boolean match(String str, String pattern) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}

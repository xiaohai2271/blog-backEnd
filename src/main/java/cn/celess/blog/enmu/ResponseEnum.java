package cn.celess.blog.enmu;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:37
 */
public enum ResponseEnum {
    // Response enum

    SUCCESS(0, "成功"),
    FAILURE(-1, "失败"),
    ERROR(-2, "错误"),

    //文章类
    ARTICLE_NOT_EXIST(201, "文章不存在"),
    ARTICLE_HAS_EXIST(202, "文章已存在"),
    ARTICLE_NOT_PUBLIC(203, "文章暂未公开"),
    ARTICLE_NOT_BELONG_YOU(204, "无权限操作别人的文章"),

    //用户类
    HAVE_NOT_LOG_IN(301, "还未登录"),
    PERMISSION_ERROR(302, "没有此权限"),
    USER_NOT_EXIST(303, "用户不存在"),
    USERNAME_HAS_EXIST(304, "用户名已存在"),
    USERNAME_TOO_SHORT(305, "用户名太短"),
    PASSWORD_TOO_SHORT_OR_LONG(306, "密码长度过长或者过短"),
    LOGIN_FAILURE(310, "登录失败,用户名/密码不正确"),
    USEREMAIL_NULL(331, "未设置邮箱"),
    USEREMAIL_NOT_VERIFY(332, "邮箱未验证"),
    LOGIN_LATER(350, "错误次数已达5次，请稍后再试"),
    PWD_SAME(360, "新密码与原密码相同"),
    LOGIN_EXPIRED(370, "登陆过期"),

    JWT_EXPIRED(381, "Token过期"),
    JWT_MALFORMED(382, "Token格式不对"),
    JWT_SIGNATURE(383, "Token签名错误"),
    JWT_NOT_SUPPORT(384, "不支持的Token"),

    //标签
    TAG_NOT_EXIST(401, "标签不存在"),
    TAG_HAS_EXIST(402, "标签已存在"),

    //分类
    CATEGORY_NOT_EXIST(501, "分类不存在"),
    CATEGORY_HAS_EXIST(502, "分类已存在"),

    //评论/留言
    COMMENT_NOT_EXIST(601, "评论/留言不存在"),
    COMMENT_HAS_EXIST(602, "评论/留言已存在,请不要重复提交"),

    //webUdpateInfo   amd  PartnerSite
    DATA_NOT_EXIST(701, "数据不存在"),
    DATA_HAS_EXIST(702, "数据已存在"),

    //其他

    //提交更新之前，没有获取数据/,
    DID_NOT_GET_THE_DATA(802, "非法访问"),
    IMG_CODE_TIMEOUT(810, "验证码已失效"),
    IMG_CODE_DIDNOTVERIFY(820, "请先验证验证码"),
    VERIFY_ERROR(830, "验证失败"),
    PARAMETERS_ERROR(850, "参数错误"),
    PARAMETERS_URL_ERROR(851, "链接格式错误"),
    PARAMETERS_EMAIL_ERROR(852, "邮箱格式错误"),
    PARAMETERS_PHONE_ERROR(853, "手机格式错误"),
    PARAMETERS_QQ_ERROR(854, "QQ格式错误"),
    PARAMETERS_PWD_ERROR(855, "密码格式错误"),
    VERIFY_OUT(840, "已经验证过了");

    private int code;
    private String msg;


    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

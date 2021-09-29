package cn.celess.common.enmu;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:37
 */
public enum ResponseEnum {
    // Response enum

    SUCCESS(0, "成功"),
    FAILURE(-1, "失败"),
    ERROR(-2, "错误"),

    DATA_IS_DELETED(1000, "数据已被删除"),

    //文章类
    ARTICLE_NOT_EXIST(2010, "文章不存在"),
    ARTICLE_HAS_EXIST(2020, "文章已存在"),
    ARTICLE_NOT_PUBLIC(2030, "文章暂未公开"),
    ARTICLE_NOT_BELONG_YOU(2040, "无权限操作别人的文章"),

    //用户类
    HAVE_NOT_LOG_IN(3010, "还未登录"),
    PERMISSION_ERROR(3020, "没有此权限"),
    USER_NOT_EXIST(3030, "用户不存在"),
    USERNAME_HAS_EXIST(3040, "用户名已存在"),
    USERNAME_TOO_SHORT(3050, "用户名太短"),
    PASSWORD_TOO_SHORT_OR_LONG(3060, "密码长度过长或者过短"),
    LOGIN_FAILURE(3100, "登录失败,用户名/密码不正确"),
    USEREMAIL_NULL(3310, "未设置邮箱"),
    USEREMAIL_NOT_VERIFY(3320, "邮箱未验证"),
    LOGIN_LATER(3500, "错误次数已达5次，请稍后再试"),
    PWD_SAME(3601, "新密码与原密码相同"),
    PWD_NOT_SAME(3602, "新密码与原密码不相同"),
    LOGIN_EXPIRED(3700, "登陆过期"),
    LOGOUT(3710, "账户已注销"),
    CAN_NOT_USE(3711, "账户不可用"),
    PWD_WRONG(3800, "密码不正确"),

    JWT_EXPIRED(3810, "Token过期"),
    JWT_MALFORMED(3820, "Token格式不对"),
    JWT_SIGNATURE(3830, "Token签名错误"),
    JWT_NOT_SUPPORT(3840, "不支持的Token"),

    //标签
    TAG_NOT_EXIST(4010, "标签不存在"),
    TAG_HAS_EXIST(4020, "标签已存在"),

    //分类
    CATEGORY_NOT_EXIST(5010, "分类不存在"),
    CATEGORY_HAS_EXIST(5020, "分类已存在"),

    //评论/留言
    COMMENT_NOT_EXIST(6010, "评论/留言不存在"),
    COMMENT_HAS_EXIST(6020, "评论/留言已存在,请不要重复提交"),

    //webUdpateInfo   amd  PartnerSite
    DATA_NOT_EXIST(7010, "数据不存在"),
    DATA_HAS_EXIST(7020, "数据已存在"),

    //其他
    APPLY_LINK_NO_ADD_THIS_SITE(7200, "暂未在您的网站中抓取到本站链接"),
    DATA_EXPIRED(7300, "数据过期"),
    CANNOT_GET_DATA(7400, "暂无法获取到数据"),
    NO_FILE(7500, "未选择文件，请重新选择"),


    //提交更新之前，没有获取数据/,
    DID_NOT_GET_THE_DATA(8020, "非法访问"),
    IMG_CODE_TIMEOUT(8100, "验证码已失效"),
    IMG_CODE_DIDNOTVERIFY(8200, "请先验证验证码"),
    VERIFY_ERROR(8300, "验证失败"),
    PARAMETERS_ERROR(8500, "参数错误"),
    PARAMETERS_URL_ERROR(8510, "链接格式错误"),
    PARAMETERS_EMAIL_ERROR(8520, "邮箱格式错误"),
    PARAMETERS_PHONE_ERROR(8530, "手机格式错误"),
    PARAMETERS_QQ_ERROR(8540, "QQ格式错误"),
    PARAMETERS_PWD_ERROR(8550, "密码格式错误"),
    VERIFY_OUT(8400, "已经验证过了");
    private final int code;
    private final String msg;


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

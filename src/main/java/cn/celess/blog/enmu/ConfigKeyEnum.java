package cn.celess.blog.enmu;

/**
 * @author : xiaohai
 * @date : 2020/10/16 16:41
 * @desc :
 */
public enum ConfigKeyEnum {

    /**
     * 枚举
     */
    FILE_TYPE("file.type"),
    FILE_QINIU_ACCESS_KEY("file.qiniu.accessKey"),
    FILE_QINIU_SECRET_KEY("file.qiniu.secretKey"),
    FILE_QINIU_BUCKET("file.qiniu.bucket"),
    FILE_LOCAL_DIRECTORY_PATH("file.local.directoryPath"),
    BLOG_FILE_PATH("blog.file.path"),
    DB_TYPE("db.type");


    private final String key;

    ConfigKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

package cn.celess.blog.entity.model;

/**
 * @author : xiaohai
 * @date : 2020/10/15 22:16
 * @desc :
 */
public class FileInfo {
    /**
     * 文件名
     */
    public String key;
    /**
     * 文件hash值
     */
    public String hash;
    /**
     * 文件大小，单位：字节
     */
    public long size;
    /**
     * 文件上传时间，单位为：100纳秒
     */
    public long uploadTime;
}

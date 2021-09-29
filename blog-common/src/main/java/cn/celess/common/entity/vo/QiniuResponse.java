package cn.celess.common.entity.vo;


import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/04/21 22:43
 */
public class QiniuResponse implements Serializable {
    public String key;
    public String hash;
    public String bucket;
    public long fsize;
}

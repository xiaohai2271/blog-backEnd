package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2020/07/31 20:50
 */
@Data
public class LinkApplyReq {
    private String name;
    private String email;
    private String url;
    private String linkUrl;
    private String desc;
    private String iconPath;
}

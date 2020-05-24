package cn.celess.blog.entity;

import lombok.Data;

/**
 * 友链
 *
 * @author : xiaohai
 * @date : 2019/05/12 11:33
 */
@Data
public class PartnerSite {

    private Long id;

    private String name;

    private String url;

    private Boolean open;

    private String iconPath;

    private String desc;

    private boolean delete;

    public PartnerSite() {
    }

    public PartnerSite(String name, String url, Boolean open) {
        this.name = name;
        this.url = url;
        this.open = open;
    }
}

package cn.celess.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 友链
 *
 * @author : xiaohai
 * @date : 2019/05/12 11:33
 */
@Data
public class PartnerSite implements Serializable {

    private Long id;

    private String name;

    private String url;

    private Boolean open;

    private String iconPath;

    private String desc;

    private Boolean delete = false;

    private String email;

    private Boolean notification = true;

    public PartnerSite() {
    }

    public PartnerSite(String name, String url, Boolean open) {
        this.name = name;
        this.url = url;
        this.open = open;
    }
}

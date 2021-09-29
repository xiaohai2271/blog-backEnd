package cn.celess.common.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2020/07/31 20:50
 */
@Data
public class LinkApplyReq implements Serializable {
    private String name;
    private String email;
    private String url;
    private String linkUrl;
    private String desc;
    private String iconPath;
}

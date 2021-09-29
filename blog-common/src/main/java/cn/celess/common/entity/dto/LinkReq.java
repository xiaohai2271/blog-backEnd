package cn.celess.common.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/06/02 11:40
 */
@Data
public class LinkReq implements Serializable {
    private long id;
    private String name;
    private String url;
    private String iconPath;
    private String desc;
    private boolean open;
}

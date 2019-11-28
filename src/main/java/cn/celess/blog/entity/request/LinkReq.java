package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/06/02 11:40
 */
@Data
public class LinkReq {
    private long id;
    private String name;
    private String url;
    private boolean open;
}

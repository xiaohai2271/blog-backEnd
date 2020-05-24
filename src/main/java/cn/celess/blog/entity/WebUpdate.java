package cn.celess.blog.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:29
 */
@Data
public class WebUpdate {

    private long id;

    private String updateInfo;

    private Date updateTime;

    private boolean delete;

    public WebUpdate() {
    }

    public WebUpdate(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}

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

    public WebUpdate() {
    }

    public WebUpdate(String updateInfo, Date updateTime) {
        this.updateInfo = updateInfo;
        this.updateTime = updateTime;
    }
}

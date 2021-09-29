package cn.celess.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:29
 */
@Data
public class WebUpdate implements Serializable {

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

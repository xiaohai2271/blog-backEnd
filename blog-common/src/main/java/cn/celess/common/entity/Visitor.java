package cn.celess.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/04/02 22:14
 */
@Data
public class Visitor implements Serializable {

    private long id;
    private String ip;
    private Date date;
    private String ua;
    private boolean delete;

    public Visitor(String ip, Date date, String ua) {
        this.ip = ip;
        this.date = date;
        this.ua = ua;
    }

    public Visitor() {
    }
}

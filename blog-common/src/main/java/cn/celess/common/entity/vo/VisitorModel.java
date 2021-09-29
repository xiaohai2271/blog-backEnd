package cn.celess.common.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/05/05 16:05
 */
@Data
public class VisitorModel implements Serializable {
    private long id;

    private String ip;

    private String date;

    private String browserName;

    private String browserVersion;

    private String OSName;

    private String location;
}

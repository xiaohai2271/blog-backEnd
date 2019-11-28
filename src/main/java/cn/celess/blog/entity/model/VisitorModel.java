package cn.celess.blog.entity.model;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/05/05 16:05
 */
@Data
public class VisitorModel {
    private long id;

    private String ip;

    private String date;

    private String browserName;

    private String browserVersion;

    private String OSName;

    private String location;
}

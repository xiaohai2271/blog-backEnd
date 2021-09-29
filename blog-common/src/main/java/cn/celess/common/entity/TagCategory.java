package cn.celess.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 小海
 * @Date: 2020-05-24 14:03
 * @Desc:
 */
@Data
public class TagCategory implements Serializable {
    private Long id;

    private String name;

    private Boolean category = true;

    private Boolean deleted = false;
}

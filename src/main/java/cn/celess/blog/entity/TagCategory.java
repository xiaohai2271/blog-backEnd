package cn.celess.blog.entity;

import lombok.Data;

/**
 * @Author: 小海
 * @Date: 2020-05-24 14:03
 * @Desc:
 */
@Data
public class TagCategory {
    private Long id;

    private String name;

    private boolean category = true;

    private boolean deleted = false;
}

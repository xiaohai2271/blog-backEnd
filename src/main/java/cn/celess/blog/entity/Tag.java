package cn.celess.blog.entity;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:19
 */
@Data
public class Tag {
    private Long id;

    private String name;

    private String articles;
}

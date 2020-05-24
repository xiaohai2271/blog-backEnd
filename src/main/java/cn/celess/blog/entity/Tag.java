package cn.celess.blog.entity;

import lombok.NoArgsConstructor;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:19
 */
@NoArgsConstructor
public class Tag extends TagCategory {

    public Tag(String name) {
        super.setName(name);
    }
}

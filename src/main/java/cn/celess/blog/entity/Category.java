package cn.celess.blog.entity;

import lombok.NoArgsConstructor;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:18
 */
@NoArgsConstructor
public class Category extends TagCategory {
    public Category(String name) {
        super.setName(name);
    }
}

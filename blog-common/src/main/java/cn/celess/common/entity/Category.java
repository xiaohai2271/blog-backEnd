package cn.celess.common.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:18
 */
@NoArgsConstructor
public class Category extends TagCategory implements Serializable {
    public Category(String name) {
        super.setName(name);
    }
}

package cn.celess.common.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:19
 */
@NoArgsConstructor
public class Tag extends TagCategory implements Serializable {

    public Tag(String name) {
        super.setName(name);
    }
}

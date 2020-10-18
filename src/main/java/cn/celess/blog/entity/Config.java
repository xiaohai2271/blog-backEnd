package cn.celess.blog.entity;

import cn.celess.blog.enmu.ConfigKeyEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaohai
 * @date : 2020/10/16 15:24
 * @desc :
 */
@Data
@NoArgsConstructor
public class Config {
    private Integer id;
    private String name;
    private String value;

    public Config(String name) {
        this.name = name;
    }

    public Config(ConfigKeyEnum e) {
        this.name = e.getKey();
    }
}

package cn.celess.blog.enmu;

import lombok.Getter;

/**
 * @Author: 小海
 * @Date： 2019/06/29 00:00
 * @Description： 文章数据模型转换的级别（响应参数的选择）
 */
@Getter
public enum LevelEnum {
    //低级
    LOW(0),
    //中级
    MIDDLE(1),
    //另一个级别的转化
    BETWEEN_M_AND_H(2),
    //高级
    HEIGHT(3);

    private int levelCode;

    LevelEnum(int levelCode) {
        this.levelCode = levelCode;
    }

}

package cn.celess.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 小海
 * @Date: 2020-03-29 12:18
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel implements Serializable {
    private Long id;

    private String name;

    private List<ArticleModel> articles;
    private boolean deleted;
}

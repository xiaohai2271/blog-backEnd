package cn.celess.blog.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 小海
 * @Date: 2020-03-29 13:56
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagModel {
    private Long id;

    private String name;

    private List<ArticleModel> articles;

}

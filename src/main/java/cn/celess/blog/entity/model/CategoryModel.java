package cn.celess.blog.entity.model;

import cn.celess.blog.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 小海
 * @Date: 2020-03-29 12:18
 * @Desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    private Long id;

    private String name;

    private List<Article> articles;
}

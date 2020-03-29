package cn.celess.blog.entity.model;

import cn.celess.blog.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
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

    private List<Integer> articles;


    public CategoryModel(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        if (category.getArticles() == null || category.getArticles().length() == 0) {
            articles = null;
        } else {
            articles = new ArrayList<>();
            for (String s : category.getArticles().split(",")) {
                if ("".equals(s)) {
                    return;
                }
                articles.add(Integer.parseInt(s));
            }
        }
    }
}

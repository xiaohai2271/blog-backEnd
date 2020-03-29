package cn.celess.blog.entity.model;

import cn.celess.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    private List<Integer> articles;

    public TagModel(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        if (tag.getArticles() == null || tag.getArticles().length() == 0) {
            articles = null;
        } else {
            articles = new ArrayList<>();
            for (String s : tag.getArticles().split(",")) {
                if ("".equals(s)) {
                    return;
                }
                articles.add(Integer.parseInt(s));
            }
        }
    }
}

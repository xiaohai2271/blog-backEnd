package cn.celess.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: 小海
 * @Date: 2020-05-24 14:52
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag implements Serializable {
    private Long id;

    private Article article;

    private TagCategory tag;

    public ArticleTag(Article article, TagCategory tag) {
        this.article = article;
        this.tag = tag;
    }
}

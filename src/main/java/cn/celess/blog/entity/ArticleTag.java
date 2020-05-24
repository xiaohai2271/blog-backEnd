package cn.celess.blog.entity;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 小海
 * @Date: 2020-05-24 14:52
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag {
    private Long id;

    private Article article;

    private TagCategory tag;

    public ArticleTag(Article article, TagCategory tag) {
        this.article = article;
        this.tag = tag;
    }
}

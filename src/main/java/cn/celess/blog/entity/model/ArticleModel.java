package cn.celess.blog.entity.model;

import cn.celess.blog.entity.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/04/23 12:02
 */
@Getter
@Setter
public class ArticleModel {
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * Markdown正文
     */
    private String mdContent;

    /**
     * 文章类型 true(1)为原创  false(0)为转载
     */
    private Boolean original;

    /**
     * 若为转载 则为转载文章的url
     */
    private String url;

    /**
     * 发布时间
     */
    private String publishDateFormat;

    /**
     * 更新时间
     */
    private String updateDateFormat;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<Tag> tags;

    /**
     * 作者
     */
    private UserModel author;

    private ArticleModel preArticle;

    private ArticleModel nextArticle;

    /**
     * 阅读数
     */
    private Long readingNumber;

    private Integer likeCount;

    private Integer dislikeCount;

    /**
     * 文章的状态  true：公开  false:不公开
     */
    private Boolean open;
}

package cn.celess.blog.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/03/28 14:51
 */
@Data
public class Article {
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
    private Boolean type;

    /**
     * 若为转载 则为转载文章的url
     */
    private String url = null;

    private Date publishDate;

    private Date updateDate = null;

    private Long categoryId;

    private String tagsId;

    private Long authorId;

    private Long preArticleId;

    private Long nextArticleId;

    private Long readingNumber;

    /**
     * 文章的状态  true：公开  false:不公开
     */
    private Boolean open;

}

package cn.celess.blog.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/04/22 21:50
 */
@Setter
@Getter
public class CommentModel {
    private long id;

    /**
     * 是评论还是留言 0:评论  其他（1）：留言
     */
    private boolean isComment;

    private String authorName;

    private String authorAvatarImgUrl;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章ID
     */
    private long articleID;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 发布日期
     */
    private String date;

    /**
     * 回应着ID  默认为顶级回复
     */
    private String responseId = "";

    /**
     * 评论的父ID
     */
    private long pid = -1;


}

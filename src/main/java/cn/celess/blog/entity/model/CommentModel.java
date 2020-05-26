package cn.celess.blog.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/04/22 21:50
 */
@Setter
@Getter
public class CommentModel {
    private long id;

    private UserModel fromUser;

    private UserModel toUser;

    /**
     * 内容
     */
    private String content;

    /**
     * 文章标题
     */
    private String pagePath;

    /**
     * 发布日期
     */
    private String date;

    /**
     * 评论的父ID
     */
    private Long pid;

    private List<CommentModel> respComment;

    private int status;
}

package cn.celess.blog.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author : xiaohai
 * @date : 2019/03/29 16:47
 */

@Data
public class Comment {

    private Long id;

    /**
     * 是评论还是留言 0:评论  其他（1）：留言
     */
    private Boolean type;

    private Long authorID;

    private String content;

    private Long articleID;

    private Date date;

    /**
     * 回应着ID  默认为顶级回复
     */
    private String responseId = "";

    /**
     * 评论的父ID
     */
    private Long pid;

}

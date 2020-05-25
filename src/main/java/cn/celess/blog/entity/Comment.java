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

    private int status;

    private String pagePath;

    private String content;

    private Date date;

    private User fromUser;

    private User toUser;
    /**
     * 评论的父ID
     */
    private Long pid;

    private boolean delete;
}

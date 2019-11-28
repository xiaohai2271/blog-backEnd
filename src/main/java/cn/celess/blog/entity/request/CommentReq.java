package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/06/02 10:35
 */
@Data
public class CommentReq {
    private Long id;
    private Boolean comment;
    private String content;
    private Long pid;
    private Long articleID;
    private String responseId;
}

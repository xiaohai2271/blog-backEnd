package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/06/02 10:35
 */
@Data
public class CommentReq {
    private Long id;
    private String content;
    private long pid = -1;
    private String pagePath;
    private long toUserId = -1;
}

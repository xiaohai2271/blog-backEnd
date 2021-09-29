package cn.celess.common.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/06/02 10:35
 */
@Data
public class CommentReq implements Serializable {
    private Long id;
    private String content;
    private long pid = -1;
    private String pagePath;
    private long toUserId = -1;
}

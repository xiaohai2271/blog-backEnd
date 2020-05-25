package cn.celess.blog.entity.request;

import lombok.Data;

/**
 * @author : xiaohai
 * @date : 2019/06/01 22:46
 */
@Data
public class ArticleReq {
    private Long id;
    private String title;
    private String mdContent;
    private String[] tags;
    private Boolean type;
    private String url;
    private String category;
    private Boolean open = true;
}

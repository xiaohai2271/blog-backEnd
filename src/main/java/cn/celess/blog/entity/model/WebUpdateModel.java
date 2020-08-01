package cn.celess.blog.entity.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:32
 */
@Data
@NoArgsConstructor
public class WebUpdateModel {
    private long id;

    private String info;

    private String time;

    public WebUpdateModel(long id, String info, String time) {
        this.id = id;
        this.info = info;
        this.time = time;
    }

    private boolean deleted;
}

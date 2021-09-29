package cn.celess.common.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:32
 */
@Data
@NoArgsConstructor
public class WebUpdateModel implements Serializable {
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

package cn.celess.blog.entity.model;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 小海
 * @Date: 2020-05-25 17:13
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> {

    private List<T> list;

    private long total;

    private int pageSize;

    private int pageNum;

    public PageData(PageInfo pageInfo) {
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
    }

    public PageData(PageInfo pageInfo, List<T> data) {
        this(pageInfo);
        this.list = data;
    }
}

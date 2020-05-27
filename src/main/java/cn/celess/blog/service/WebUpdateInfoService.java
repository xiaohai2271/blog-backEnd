package cn.celess.blog.service;

import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.WebUpdateModel;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:42
 */
@Service
public interface WebUpdateInfoService {
    /**
     * 新增记录
     *
     * @param info 更新内容
     * @return 创建状态
     */
    WebUpdateModel create(String info);

    /**
     * 删除数据
     *
     * @param id 数据id
     * @return 删除状态
     */
    Boolean del(long id);

    /**
     * 更新数据
     *
     * @param id   数据id
     * @param info 新内容
     * @return 数据
     */
    WebUpdateModel update(long id, String info);

    /**
     * 分页获取更新记录
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageData<WebUpdateModel> pages(int count, int page);

    /**
     * 获取全部的更新记录
     *
     * @return 更新记录
     */
    List<WebUpdateModel> findAll();

    /**
     * 获取最后更新时间
     *
     * @return
     */
    JSONObject getLastestUpdateTime();
}

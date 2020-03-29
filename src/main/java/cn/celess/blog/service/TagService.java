package cn.celess.blog.service;

import cn.celess.blog.entity.Tag;
import cn.celess.blog.entity.model.TagModel;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:23
 */
@Service
public interface TagService {
    /**
     * 新增数据
     *
     * @param name 标签名
     * @return 新增后的数据
     */
    TagModel create(String name);

    /**
     * 新增数据
     *
     * @param tag tag对象
     * @return 新增后的数据
     */

    TagModel create(Tag tag);

    /**
     * 删除数据
     *
     * @param tagId 标签id
     * @return 删除状态
     */
    boolean delete(long tagId);

    /**
     * 更新数据
     *
     * @param id   标签id
     * @param name 改名的name值
     * @return 更新后的数据
     */
    TagModel update(Long id, String name);

    /**
     * 查询单个标签信息
     *
     * @param tagId id
     * @return 标签的数据
     */
    TagModel retrieveOneById(long tagId);

    /**
     * 通过name查询标签的信息
     *
     * @param name tag的名称
     * @return 标签数据
     */
    TagModel retrieveOneByName(String name);


    /**
     * 分页获取标签数据
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo<TagModel> retrievePage(int page, int count);

    /**
     * 获取全部标签数据
     *
     * @return 标签数据列表
     */
    List<TagModel> findAll();

}

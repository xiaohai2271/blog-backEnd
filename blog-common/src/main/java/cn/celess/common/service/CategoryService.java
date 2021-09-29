package cn.celess.common.service;

import cn.celess.common.entity.vo.CategoryModel;
import cn.celess.common.entity.vo.PageData;
import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:42
 */
@Service
public interface CategoryService {
    /**
     * 增加一个分类
     *
     * @param name 分类名
     * @return 所增加的分类数据
     */
    CategoryModel create(String name);

    /**
     * 通过id删除分类
     *
     * @param id 分类id
     * @return 删除状态
     */
    boolean delete(long id);

    /**
     * 编辑分类的名字
     *
     * @param id   分类id
     * @param name 分类名字
     * @return 更新后的分类的数据
     */
    CategoryModel update(Long id, String name);

    /**
     * 获取全部的分类数据
     *
     * @return 全部的分类数据
     */
    PageData<CategoryModel> retrievePage(int page, int count);

}

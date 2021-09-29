package cn.celess.common.mapper;

import cn.celess.common.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/06/30 12:56
 * @Description：
 */
@Mapper
@Repository
public interface CategoryMapper {
    int insert(Category c);

    int delete(long id);

    int update(Category c);

    boolean existsByName(String name);

    boolean existsById(long id);

    Category findCategoryByName(String name);

    Category findCategoryById(long id);

    List<Category> findAll();

    List<String> getAllName();

    String getNameById(long id);

    Long getIdByName(String name);

    Category getLastestCategory();

    long count();
}

package cn.celess.common.mapper;

import cn.celess.common.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/06/29 22:00
 * @Description：
 */
@Mapper
@Repository
public interface TagMapper {
    int insert(Tag tag);

    int update(Tag tag);

    int delete(long id);

    Tag findTagById(long id);

    Tag findTagByName(String name);

    Boolean existsByName(String name);

    Tag getLastestTag();

    List<Tag> findAll();

    long count();
}

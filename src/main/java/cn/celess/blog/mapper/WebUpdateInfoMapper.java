package cn.celess.blog.mapper;

import cn.celess.blog.entity.WebUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/07/03 00:24
 * @Description：
 */
@Mapper
@Repository
public interface WebUpdateInfoMapper {
    int insert(WebUpdate webUpdate);

    int delete(long id);

    int update(long id, String info);

    boolean existsById(long id);

    WebUpdate findById(long id);

    List<WebUpdate> findAll();

    Date getLastestOne();
}

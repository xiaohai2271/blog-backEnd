package cn.celess.common.mapper;

import cn.celess.common.entity.PartnerSite;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/07/03 00:22
 * @Description：
 */
@Mapper
@Repository
public interface PartnerMapper {
    int insert(PartnerSite site);

    int delete(long id);

    int update(PartnerSite site);

    boolean existsById(long id);

    boolean existsByName(String name);

    boolean existsByUrl(String url);

    PartnerSite findById(long id);

    PartnerSite findByName(String name);

    PartnerSite findByUrl(String url);

    PartnerSite getLastest();

    List<PartnerSite> findAll();

    List<PartnerSite> findAll(Boolean deleted);

}

package cn.celess.blog.mapper;

import cn.celess.blog.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2020/10/16 15:24
 * @desc :
 */
@Mapper
@Repository
public interface ConfigMapper {
    /**
     * 获取单个配置
     *
     * @param key 配置名
     * @return 配置
     */
    Config getConfiguration(String key);

    /**
     * 更新配置
     *
     * @param c 配置
     * @return 改变数据行数
     */
    int updateConfiguration(Config c);

    /**
     * 获取所有配置
     *
     * @return 所有配置
     */
    List<Config> getConfigurations();

    /**
     * 新增配置
     *
     * @param c 配置
     * @return 改变行数
     */
    int addConfiguration(Config c);

    /**
     * 删除配置
     *
     * @param id 主键id
     * @return 改变行数
     */
    int deleteConfiguration(int id);
}

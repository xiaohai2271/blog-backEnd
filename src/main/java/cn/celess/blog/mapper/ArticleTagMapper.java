package cn.celess.blog.mapper;

import cn.celess.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date: 2020-05-24 14:21
 * @Desc:
 */
@Mapper
@Repository
public interface ArticleTagMapper {

    int insert(ArticleTag articleTag);

    int update(ArticleTag articleTag);

    ArticleTag findOneById(Long id);

    int deleteById(Long id);

    int deleteByArticleId(Long articleId);

    List<ArticleTag> findAllByArticleId(Long articleId);

    int deleteMultiById(List<ArticleTag> articleTags);
}

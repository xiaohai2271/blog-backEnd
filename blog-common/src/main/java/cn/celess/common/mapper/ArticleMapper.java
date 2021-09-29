package cn.celess.common.mapper;

import cn.celess.common.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/06/27 20:43
 * @Description：
 */
@Mapper
@Repository
public interface ArticleMapper {

    int insert(Article a);

    int delete(long id);

    int update(Article a);

    Article getLastestArticle();

    Article findArticleById(long id);

    boolean existsByTitle(String title);

    boolean isDeletedById(long id);

    List<Article> findAllByAuthorId(long authorId);

    List<Article> findAllByOpen(boolean isOpen);

    String getTitleById(long id);

    List<Article> findAllByCategoryId(long id);

    List<Article> findAllByCategoryIdAndOpen(long id);

    List<Article> findAll();

    @Cacheable(value = {"articleDao"}, key = "'getPreArticle:'+#id")
    Article getPreArticle(Long id);

    @Cacheable(value = {"articleDao"}, key = "'getNextArticle:'+#id")
    Article getNextArticle(Long id);

    int updateReadingNumber(long id);

    long count();

}

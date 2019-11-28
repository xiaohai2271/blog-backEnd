package cn.celess.blog.mapper;

import cn.celess.blog.entity.Article;
import org.apache.ibatis.annotations.*;
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

    int updateNextArticleId(long targetArticleID, long nextArticleID);

    int updatePreArticleId(long targetArticleID, long preArticleID);

    long getLastestArticleId();

    Article getLastestArticle();

    Article findArticleById(long id);

    boolean existsByTitle(String title);

    boolean existsById(long id);

    List<Article> findAllByAuthorId(long authorID);

    List<Article> findAllByOpen(boolean isOpen);

    String getTitleById(long id);

    List<Article> findAllByCategoryId(long id);

    List<Article> findAll();

    Article getSimpleInfo(long id);

    List<Article> getSimpleInfoByCategory(long categoryId);

    List<Article> getSimpleInfoByTag(List<String> idList);

    int setReadingNumber(long number, long id);

    long count();

}

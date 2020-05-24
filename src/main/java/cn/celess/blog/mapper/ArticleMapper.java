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

    Article getLastestArticle();

    Article findArticleById(long id);

    boolean existsByTitle(String title);

    boolean isDeletedById(long id);

    List<Article> findAllByAuthorId(long authorId);

    List<Article> findAllByOpen(boolean isOpen);

    String getTitleById(long id);

    List<Article> findAllByCategoryId(long id);

    List<Article> findAll();

    int updateReadingNumber(long id);

    long count();

}

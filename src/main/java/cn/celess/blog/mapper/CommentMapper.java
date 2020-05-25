package cn.celess.blog.mapper;

import cn.celess.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date: 2019/06/30 16:19
 * @Description:
 */
@Mapper
@Repository
public interface CommentMapper {
    int insert(Comment c);

    int updateContent(String content, long id);

    int delete(long id);

    int deleteByPagePath(String pagePath);

    boolean existsById(long id);

    Comment findCommentById(long id);

    Comment getLastestComment();

    List<Comment> findAllByFromUser(long id);

    List<Comment> findAllByPid(long pid);

    List<Comment> findAllByPagePath(String pagePath);

    List<Comment> findAllByPagePathAndPid(String pagePath, long pid);

    long countByPagePath(String pagePath);

    long count();
}

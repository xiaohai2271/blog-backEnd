package cn.celess.blog.mapper;

import cn.celess.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/06/30 16:19
 * @Description：
 */
@Mapper
@Repository
public interface CommentMapper {
    int insert(Comment c);

    int updateContent(String content, long id);

    int updateResponder(String responder, long id);

    int delete(long id);

    int deleteByArticleId(long articleId);

    boolean existsById(long id);

    Comment findCommentById(long id);

    Comment getLastestComment();

    List<Comment> findAllByAuthorIDAndType(long id, boolean isComment);

    List<Comment> findAllByPId(long pid);

    List<Comment> findAllByArticleID(long articleId);

    List<Comment> findAllByArticleIDAndPId(long articleID, long pid);

    List<Comment> findCommentsByTypeAndPId(boolean isComment, long pid);

    List<Comment> findAllByPId(int pid);

    List<Comment> findAllByType(boolean isComment);

    long countByType(boolean isComment);
}

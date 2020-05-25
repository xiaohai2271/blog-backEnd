package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class CommentMapperTest extends BaseTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;

    @Test
    public void insert() {
        Comment comment = generateComment();
        assertNotNull(comment.getId());
    }

    @Test
    public void updateContent() {
        Comment comment = generateComment();
        comment.setContent(randomStr(10));
        assertEquals(1, commentMapper.updateContent(comment.getContent(), comment.getId()));
    }

    @Test
    public void delete() {
        Comment comment = generateComment();
        assertEquals(1, commentMapper.delete(comment.getId()));
        Comment commentById = commentMapper.findCommentById(comment.getId());
        assertTrue(commentById.isDelete());
    }

    @Test
    public void deleteByPagePath() {
        Comment comment = generateComment();
        assertTrue(commentMapper.deleteByPagePath(comment.getPagePath()) >= 1);
        Comment commentById = commentMapper.findCommentById(comment.getId());
        assertTrue(commentById.isDelete());
    }

    @Test
    public void existsById() {
        Comment comment = generateComment();
        assertTrue(commentMapper.existsById(comment.getId()));
    }

    @Test
    public void findCommentById() {
        Comment comment = generateComment();
        assertNotNull(commentMapper.findCommentById(comment.getId()));
    }

    @Test
    public void getLastestComment() {
        Comment comment = generateComment();
        Comment lastestComment = commentMapper.getLastestComment();
        assertEquals(comment.getId(), lastestComment.getId());
    }

    @Test
    public void findAllByFromUser() {
        Comment comment = generateComment();
        List<Comment> allByFromUser = commentMapper.findAllByFromUser(comment.getFromUser().getId());
        assertNotEquals(0, allByFromUser);
        allByFromUser.forEach(comment1 -> assertEquals(comment.getFromUser().getId(), comment1.getFromUser().getId()));
    }

    @Test
    public void findAllByPid() {
        Comment comment = generateComment();
        List<Comment> allByPid = commentMapper.findAllByPid(comment.getPid());
        assertTrue(allByPid.size() >= 1);
    }

    @Test
    public void findAllByPagePath() {
        Comment comment = generateComment();
        List<Comment> allByPagePath = commentMapper.findAllByPagePath(comment.getPagePath());
        assertTrue(allByPagePath.size() >= 1);
        allByPagePath.forEach(comment1 -> assertEquals(comment.getPagePath(), comment1.getPagePath()));
    }

    @Test
    public void findAllByPagePathAndPid() {
        Comment comment = generateComment();
        List<Comment> allByPagePathAndPid = commentMapper.findAllByPagePathAndPid(comment.getPagePath(), comment.getPid());
        assertTrue(allByPagePathAndPid.size() >= 1);
        allByPagePathAndPid.forEach(comment1 -> {
            assertEquals(comment.getPagePath(), comment1.getPagePath());
            assertEquals(comment.getPid(), comment1.getPid());
        });
    }

    @Test
    public void testFindAllByPid() {
        Comment comment = generateComment();
        List<Comment> findAllByPid = commentMapper.findAllByPid(comment.getPid());
        assertTrue(findAllByPid.size() >= 1);
        findAllByPid.forEach(comment1 -> assertEquals(comment.getPid(), comment1.getPid()));
    }

    @Test
    public void countByType() {
        Comment comment = generateComment();
        long l = commentMapper.countByPagePath(comment.getPagePath());
        assertTrue(l >= 1);
    }

    private Comment generateComment() {
        User from = userMapper.findById(1);
        assertNotNull(from);
        User to = userMapper.findById(2);
        assertNotNull(to);

        Comment comment = new Comment();
        comment.setContent(randomStr(8));
        comment.setFromUser(from);
        comment.setToUser(to);
        comment.setPagePath("/tags");
        comment.setPid(-1L);
        commentMapper.insert(comment);
        return comment;
    }
}
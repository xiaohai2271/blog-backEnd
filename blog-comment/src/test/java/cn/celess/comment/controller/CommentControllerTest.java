package cn.celess.comment.controller;

import cn.celess.comment.CommentBaseTest;
import cn.celess.common.entity.Article;
import cn.celess.common.entity.Comment;
import cn.celess.common.entity.Response;
import cn.celess.common.entity.User;
import cn.celess.common.entity.dto.CommentReq;
import cn.celess.common.entity.vo.CommentModel;
import cn.celess.common.mapper.ArticleMapper;
import cn.celess.common.mapper.CommentMapper;
import cn.celess.common.mapper.UserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.celess.common.constant.ResponseEnum.DATA_IS_DELETED;
import static cn.celess.common.constant.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CommentControllerTest extends CommentBaseTest {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;
    private static final TypeReference<?> COMMENT_MODEL_TYPE = new TypeReference<Response<CommentModel>>() {
    };

    @Test
    public void addOne() throws Exception {
        Article article = articleMapper.getLastestArticle();
        CommentReq commentReq = new CommentReq();
        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(randomStr());
        List<User> all = userMapper.findAll();
        commentReq.setPid(1L);
        commentReq.setToUserId(2l);
        getMockData(post("/user/comment/create"), userLogin(), commentReq).andDo(result -> {
            Response<CommentModel> response = getResponse(result, COMMENT_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CommentModel model = response.getResult();
            assertNotEquals(0, model.getId());
            assertEquals(commentReq.getPid(), model.getPid().longValue());
            assertEquals(1, model.getPid().longValue());
            assertEquals(commentReq.getContent(), model.getContent());
            assertNotNull(model.getDate());
            assertNotNull(model.getFromUser());
            assertNotNull(model.getToUser());
        });


        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(randomStr());
        commentReq.setPid(-1L);
        commentReq.setToUserId(2);
        getMockData(post("/user/comment/create"), userLogin(), commentReq).andDo(result -> {
            Response<CommentModel> response = getResponse(result, COMMENT_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CommentModel model = response.getResult();
            // 响应数据的完整性
            assertNotEquals(0, model.getId());
            assertEquals(commentReq.getPid(), model.getPid().longValue());
            assertEquals(-1, model.getPid().longValue());
            assertEquals(commentReq.getContent(), model.getContent());
            assertEquals(commentReq.getPagePath(), "/article/" + article.getId());
            assertNotNull(model.getDate());
            assertNotNull(model.getFromUser());
            assertNotNull(model.getToUser());
        });

        // 测试二级回复
        Comment latestComment = commentMapper.getLastestComment();
        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(randomStr());
        commentReq.setPid(latestComment.getId());
        getMockData(post("/user/comment/create"), userLogin(), commentReq).andDo(result -> {
            Response<CommentModel> response = getResponse(result, COMMENT_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CommentModel model = response.getResult();
            // 重新获取父评论信息
            Comment pCommon = commentMapper.findCommentById(latestComment.getId());
            assertEquals(pCommon.getId(), model.getPid());
        });
    }

    @Test
    public void deleteTest() throws Exception {
        // 准备数据
        User from = userMapper.findByEmail("zh56462271@qq.com");
        assertNotNull(from);
        User to = userMapper.findByEmail("a@celess.cn");
        assertNotNull(to);

        Comment comment = new Comment();
        comment.setContent(randomStr(8));
        comment.setFromUser(from);
        comment.setToUser(to);
        comment.setPagePath("/tags");
        comment.setPid(-1L);
        commentMapper.insert(comment);
        comment = commentMapper.findCommentById(comment.getId());
        // 接口测试
        long id = comment.getId();
        assertNotEquals(0, id);
        getMockData(delete("/user/comment/del?id=" + id), userLogin()).andDo(result -> {
            Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertTrue(response.getResult());
        });
        getMockData(delete("/user/comment/del?id=" + id), userLogin())
                .andDo(result -> assertEquals(DATA_IS_DELETED.getCode(), getResponse(result, COMMENT_MODEL_TYPE).getCode()));
    }

    @Test
    public void update() throws Exception {
        Comment comment = commentMapper.getLastestComment();
        CommentReq commentReq = new CommentReq();
        commentReq.setId(comment.getId());
        commentReq.setContent(randomStr());
        // 不合法数据 setResponseId
        getMockData(put("/user/comment/update"), userLogin(), commentReq).andDo(result -> {
            Response<CommentModel> response = getResponse(result, COMMENT_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CommentModel c = response.getResult();
            assertEquals(commentReq.getContent(), c.getContent());
        });
    }

}
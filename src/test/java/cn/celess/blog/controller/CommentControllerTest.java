package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.CommentModel;
import cn.celess.blog.entity.request.CommentReq;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.CommentMapper;
import cn.celess.blog.mapper.UserMapper;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.junit.Assert.*;
import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CommentControllerTest extends BaseTest {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;

    @Test
    public void addOne() throws Exception {
        Article article = articleMapper.getLastestArticle();
        CommentReq commentReq = new CommentReq();
        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(-1L);
        commentReq.setToUserId(-1L);
        String token = userLogin();
        mockMvc.perform(post("/user/comment/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.fromObject(commentReq).toString())
                .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel model = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            assertNotEquals(0, model.getId());
            assertEquals(commentReq.getPid(), model.getPid().longValue());
            assertEquals(-1, model.getPid().longValue());
            assertEquals(commentReq.getContent(), model.getContent());
            assertNotNull(model.getDate());
            assertNotNull(model.getFromUser());
            assertNull(model.getToUser());
        });


        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(-1L);
        commentReq.setToUserId(2);
        mockMvc.perform(post("/user/comment/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.fromObject(commentReq).toString())
                .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel model = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
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
        Comment lastestComment = commentMapper.getLastestComment();
        commentReq.setPagePath("/article/" + article.getId());
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(lastestComment.getId());
        mockMvc.perform(post("/user/comment/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.fromObject(commentReq).toString())
                .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel model = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            // 重新获取父评论信息
            Comment pCommon = commentMapper.findCommentById(lastestComment.getId());
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
        String token = userLogin();
        mockMvc.perform(delete("/user/comment/del?id=" + id).header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.getBoolean(Result));
        });
        mockMvc.perform(delete("/user/comment/del?id=" + id).header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(DATA_IS_DELETED.getCode(), object.getInt(Code));
        });
    }

    @Test
    public void update() throws Exception {
        Comment comment = commentMapper.getLastestComment();
        CommentReq commentReq = new CommentReq();
        commentReq.setId(comment.getId());
        commentReq.setContent(UUID.randomUUID().toString());
        // 不合法数据 setResponseId
        mockMvc.perform(put("/user/comment/update")
                .content(JSONObject.fromObject(commentReq).toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", userLogin())
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel c = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            assertEquals(commentReq.getContent(), c.getContent());
        });
    }

}
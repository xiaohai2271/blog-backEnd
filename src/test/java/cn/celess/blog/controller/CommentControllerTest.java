package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.model.CommentModel;
import cn.celess.blog.entity.request.CommentReq;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.CommentMapper;
import cn.celess.blog.mapper.UserMapper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CommentControllerTest extends BaseTest {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CommentMapper commentMapper;

    @Test
    public void addOne() throws Exception {
        CommentReq commentReq = new CommentReq();
        // 测试留言
        commentReq.setArticleID(null);
        commentReq.setComment(false);
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(-1L);
        commentReq.setResponseId(null);
        String token = userLogin();
        CommentModel PC = null;
        mockMvc.perform(post("/user/comment/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.fromObject(commentReq).toString())
                .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel model = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            assertNotEquals(0, model.getId());
            assertEquals(commentReq.getPid().longValue(), model.getPid());
            assertEquals(-1, model.getPid());
            assertEquals(commentReq.getComment(), model.isComment());
            assertEquals(commentReq.getContent(), model.getContent());
            assertNotNull(model.getDate());
            assertNotNull(model.getAuthorName());
            assertNotNull(model.getAuthorAvatarImgUrl());
        });

        Article article = articleMapper.getLastestArticle();
        // 测试评论
        commentReq.setArticleID(article.getId());
        commentReq.setComment(true);
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(-1L);
        commentReq.setResponseId(null);
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
            assertEquals(commentReq.getPid().longValue(), model.getPid());
            assertEquals(-1, model.getPid());
            assertEquals(commentReq.getComment(), model.isComment());
            assertEquals(commentReq.getContent(), model.getContent());
            assertEquals(commentReq.getArticleID().longValue(), model.getArticleID());
            assertNotNull(model.getDate());
            assertNotNull(model.getAuthorName());
            assertNotNull(model.getAuthorAvatarImgUrl());
        });

        // 测试二级回复
        Comment lastestComment = commentMapper.getLastestComment();
        commentReq.setArticleID(lastestComment.getArticleID());
        commentReq.setComment(lastestComment.getType());
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setPid(lastestComment.getId());
        commentReq.setResponseId(null);
        mockMvc.perform(post("/user/comment/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.fromObject(commentReq).toString())
                .header("Authorization", token)
        ).andDo(MockMvcResultHandlers.print()).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel model = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            // 重新获取父评论信息
            Comment pCommon = commentMapper.findCommentById(lastestComment.getId());
            assertEquals(pCommon.getId().longValue(), model.getPid());
            // 判断父评论中是否有写入当前新增的评论的id
            String[] ids = pCommon.getResponseId().split(",");
            boolean contain = false;
            for (String id : ids) {
                if (!id.isEmpty() && Long.parseLong(id) == model.getId()) {
                    contain = true;
                    break;
                }
            }
            assertTrue(contain);
        });
    }

    @Test
    public void deleteTest() throws Exception {
        // 准备数据
        Comment c = new Comment();
        c.setArticleID(-1L);
        c.setType(true);
        c.setAuthorID(2L);
        c.setDate(new Date());
        c.setPid(-1L);
        commentMapper.insert(c);
        Comment comment = commentMapper.getLastestComment();
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
            assertEquals(COMMENT_NOT_EXIST.getCode(), object.getInt(Code));
        });
    }

    @Test
    public void update() throws Exception {
        Comment comment = commentMapper.getLastestComment();
        CommentReq commentReq = new CommentReq();
        commentReq.setId(comment.getId());
        commentReq.setPid(comment.getPid());
        commentReq.setContent(UUID.randomUUID().toString());
        commentReq.setArticleID(comment.getArticleID());
        // 不合法数据 setResponseId
        commentReq.setResponseId("xxxx");
        commentReq.setComment(comment.getType());
        mockMvc.perform(put("/user/comment/update")
                .content(JSONObject.fromObject(commentReq).toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", userLogin())
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            CommentModel c = (CommentModel) JSONObject.toBean(object.getJSONObject(Result), CommentModel.class);
            assertEquals(commentReq.getContent(), c.getContent());
            assertEquals(commentReq.getResponseId(), c.getResponseId());
            assertNotNull(c.getAuthorAvatarImgUrl());
            assertNotNull(c.getAuthorName());
            assertNotNull(c.getDate());
            assertNotEquals(0, c.getPid());
            assertNotEquals(0, c.getArticleID());
        });
    }

    @Test
    public void commentsOfArticle() throws Exception {
        mockMvc.perform(get("/comments?articleId=3&page=1&count=10")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertEquals(3, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getArticleTitle());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
            });
        });
    }

    @Test
    public void retrievePage() throws Exception {
        long pid = 17;
        mockMvc.perform(get("/comment/pid/" + pid + "?articleId=3&page=1&count=10")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertEquals(3, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getArticleTitle());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
            });
        });
    }

    @Test
    public void retrievePageOfLeaveMsg() throws Exception {
        mockMvc.perform(get("/leaveMsg?page=1&count=10")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertEquals(-1, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
                assertFalse(model.isComment());
            });
        });
    }

    @Test
    public void retrievePageAdmin() throws Exception {
        mockMvc.perform(get("/admin/comment/type/1?page=1&count=10").header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertNotEquals(-1, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
                assertTrue(model.isComment());
            });
        });
        mockMvc.perform(get("/admin/comment/type/0?page=1&count=10").header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertEquals(-1, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
                assertFalse(model.isComment());
            });
        });
    }

    @Test
    public void retrievePageByAuthor() throws Exception {
        mockMvc.perform(get("/user/comment/type/1?page=1&count=10").header("Authorization", userLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertNotEquals(-1, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
                assertTrue(model.isComment());
            });
        });
        mockMvc.perform(get("/user/comment/type/0?page=1&count=10").header("Authorization", userLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                CommentModel model = (CommentModel) JSONObject.toBean(JSONObject.fromObject(o), CommentModel.class);
                assertEquals(-1, model.getArticleID());
                assertNotNull(model.getDate());
                assertNotNull(model.getAuthorName());
                assertNotNull(model.getAuthorAvatarImgUrl());
                assertNotNull(model.getContent());
                assertNotNull(model.getResponseId());
                assertFalse(model.isComment());
            });
        });
    }
}
package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.mapper.ArticleMapper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class ArticleControllerTest extends BaseTest {
    @Autowired
    ArticleMapper articleMapper;

    @Test
    public void create() {
        ArticleReq articleReq = new ArticleReq();
        // 应该正常通过
        articleReq.setTitle("test-" + UUID.randomUUID().toString());
        articleReq.setMdContent("# test title");
        articleReq.setCategory("随笔");
        articleReq.setTags("test,SpringMvc");
        articleReq.setOpen(true);
        articleReq.setType(true);
        articleReq.setUrl("http://xxxx.com");
        JSONObject jsonObject = JSONObject.fromObject(articleReq);

        try {
            // 未登录
            mockMvc.perform(post("/admin/article/create")
                    .content(jsonObject.toString())
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        assertEquals(HAVE_NOT_LOG_IN.getCode(),
                                JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)
                        );
                    });
            // User权限
            String token = userLogin();
            mockMvc.perform(post("/admin/article/create")
                    .content(jsonObject.toString())
                    .contentType("application/json")
                    .header("Authorization", token))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        assertEquals(PERMISSION_ERROR.getCode(),
                                JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)
                        );
                    });

            // Admin权限
            token = adminLogin();
            mockMvc.perform(post("/admin/article/create")
                    .content(jsonObject.toString())
                    .contentType("application/json")
                    .header("Authorization", token))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), object.getInt(Code));
                        ArticleModel articleModel = (ArticleModel) JSONObject.toBean(object.getJSONObject(Result), ArticleModel.class);
                        assertNotNull(articleModel.getId());
                        assertNotNull(articleModel.getTitle());
                        assertNotNull(articleModel.getSummary());
                        assertNotNull(articleModel.getOriginal());
                        assertNotNull(articleModel.getTags());
                        assertNotNull(articleModel.getCategory());
                        assertNotNull(articleModel.getPublishDateFormat());
                        assertNotNull(articleModel.getMdContent());
                        assertNotNull(articleModel.getNextArticleId());
                        assertNotNull(articleModel.getNextArticleTitle());
                        assertNotNull(articleModel.getPreArticleId());
                        assertNotNull(articleModel.getPreArticleTitle());
                        assertNotNull(articleModel.getOpen());
                        assertNotNull(articleModel.getReadingNumber());
                        assertNotNull(articleModel.getAuthorName());
                        assertNotNull(articleModel.getUrl());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        long articleId = articleMapper.getLastestArticleId();

        try {
            // 未登录删除文章
            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/article/del?articleID=" + articleId)
            ).andDo(result -> {
                assertEquals(HAVE_NOT_LOG_IN.getCode(),
                        JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)
                );
            });
            // user 权限删除文章
            String token = userLogin();
            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/article/del?articleID=" + articleId)
                    .header("Authorization", token))
                    .andDo(result -> assertEquals(PERMISSION_ERROR.getCode(),
                            JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
                    );
            // admin 权限删除文章
            token = adminLogin();
            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/article/del?articleID=" + articleId)
                    .header("Authorization", token))
                    .andDo(result -> {
                        JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), object.getInt(Code));
                        // 断言删除成功
                        assertTrue(object.getBoolean(Result));
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() {
        Article article = articleMapper.getLastestArticle();
        ArticleReq articleReq = new ArticleReq();
        articleReq.setId(article.getId());
        articleReq.setUrl("http://www.test.test");
        articleReq.setType(!article.getType());
        articleReq.setCategory("test");
        articleReq.setMdContent("test-" + article.getMdContent());
        articleReq.setOpen(!article.getOpen());
        String tag1 = randomStr(4);
        String tag2 = randomStr(4);
        String tag = "test," + tag1 + "," + tag2;
        articleReq.setTags(tag);
        articleReq.setTitle("test-" + article.getTitle());
        try {
            // Admin 权限
            mockMvc.perform(put("/admin/article/update")
                    .content(JSONObject.fromObject(articleReq).toString())
                    .contentType("application/json")
                    .header("Authorization", adminLogin()))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        JSONObject jsonObject = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), jsonObject.getInt(Code));
                        ArticleModel a = (ArticleModel) JSONObject.toBean(jsonObject.getJSONObject(Result), ArticleModel.class);
                        assertEquals(articleReq.getCategory(), a.getCategory());
                        assertEquals(articleReq.getUrl(), a.getUrl());
                        assertEquals(articleReq.getMdContent(), a.getMdContent());
                        assertEquals(articleReq.getTitle(), a.getTitle());
                        assertEquals(articleReq.getType(), a.getOriginal());
                        // Tag
                        List<String> asList = Arrays.asList(a.getTags());
                        assertTrue(asList.contains("test"));
                        assertTrue(asList.contains(tag1));
                        assertTrue(asList.contains(tag2));
                        assertEquals(articleReq.getOpen(), a.getOpen());
                        assertEquals(articleReq.getId(), a.getId());
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void retrieveOneById() {
        try {
            long articleID = 3;
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID))
                    .andExpect(status().is(200));
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=true"))
                    .andExpect(status().is(200));

            // 文章不存在
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/-1"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        JSONObject jsonObject = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(ARTICLE_NOT_EXIST.getCode(), jsonObject.getInt(Code));
                    });

            // 正常情况
            mockMvc.perform(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=false"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        JSONObject articleJson = JSONObject.fromObject(result.getResponse().getContentAsString());
                        // 断言获取数据成功
                        assertEquals(SUCCESS.getCode(), articleJson.getInt(Code));
                        assertNotNull(articleJson.getJSONObject(Result));

                        ArticleModel a = (ArticleModel) JSONObject.toBean(articleJson.getJSONObject(Result), ArticleModel.class);
                        assertNotNull(a.getTitle());
                        assertNotNull(a.getId());
                        assertNotNull(a.getSummary());
                        assertNotNull(a.getMdContent());
                        assertNotNull(a.getUrl());
                        assertNotNull(a.getUpdateDateFormat());
                        assertNotNull(a.getPreArticleId());
                        assertNotNull(a.getPreArticleId());
                        assertNotNull(a.getNextArticleId());
                        assertNotNull(a.getNextArticleTitle());
                        assertNotNull(a.getReadingNumber());
                        // assertNotNull(a.getOpen());
                        assertNotNull(a.getOriginal());
                        assertNotNull(a.getPublishDateFormat());
                        assertNotNull(a.getCategory());
                        assertNotNull(a.getTags());
                        assertNotNull(a.getAuthorName());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void articles() {
        try {
            // 测试不带参数访问
            mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                    .andExpect(status().is(200));

            mockMvc.perform(MockMvcRequestBuilders.get("/articles?page=1&count=5"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        JSONObject articlesJSON = JSONObject.fromObject(result.getResponse().getContentAsString());
                        Response response = (Response) JSONObject.toBean(articlesJSON, Response.class);
                        // 断言获取数据成功
                        assertEquals(SUCCESS.getCode(), response.getCode());
                        // 结果集非空
                        assertNotNull(response.getResult());
                        // 判断pageInfo是否包装完全
                        JSONObject resultJson = JSONObject.fromObject(response.getResult());
                        PageInfo pageInfo = (PageInfo) JSONObject.toBean(resultJson, PageInfo.class);
                        assertNotEquals(0, pageInfo.getTotal());
                        assertNotEquals(0, pageInfo.getStartRow());
                        assertNotEquals(0, pageInfo.getEndRow());
                        assertEquals(1, pageInfo.getPageNum());
                        assertEquals(5, pageInfo.getPageSize());
                        // 内容完整
                        for (Object arc : pageInfo.getList()) {
                            ArticleModel a = (ArticleModel) JSONObject.toBean(JSONObject.fromObject(arc), ArticleModel.class);
                            assertNotNull(a.getTitle());
                            assertNotNull(a.getId());
                            assertNotNull(a.getSummary());
                            assertNotNull(a.getOriginal());
                            assertNotNull(a.getPublishDateFormat());
                            assertNotNull(a.getCategory());
                            assertNotNull(a.getTags());
                            assertNotNull(a.getAuthorName());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adminArticles() {
        String token;
        try {
            // 未登录
            mockMvc.perform(get("/admin/articles?page=1&count=10"))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        assertEquals(HAVE_NOT_LOG_IN.getCode(),
                                JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)
                        );
                    });

            // User权限登陆
            token = userLogin();
            mockMvc.perform(get("/admin/articles?page=1&count=10")
                    .header("Authorization", token))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(PERMISSION_ERROR.getCode(), object.getInt(Code));
                    });

            token = adminLogin();
            // admin权限登陆
            mockMvc.perform(get("/admin/articles?page=1&count=10")
                    .header("Authorization", token))
                    .andExpect(status().isOk())
                    .andDo(result -> {
                        JSONObject adminLogin = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), adminLogin.getInt(Code));
                        assertNotNull(adminLogin.getString(Result));
                        // 判断pageInfo是否包装完全
                        PageInfo pageInfo = (PageInfo) JSONObject.toBean(adminLogin.getJSONObject(Result), PageInfo.class);
                        assertNotEquals(0, pageInfo.getTotal());
                        assertNotEquals(0, pageInfo.getStartRow());
                        assertNotEquals(0, pageInfo.getEndRow());
                        assertEquals(1, pageInfo.getPageNum());
                        assertEquals(10, pageInfo.getPageSize());
                        // 内容完整
                        for (Object arc : pageInfo.getList()) {
                            ArticleModel a = (ArticleModel) JSONObject.toBean(JSONObject.fromObject(arc), ArticleModel.class);
                            assertNotNull(a.getTitle());
                            assertNotNull(a.getId());
                            assertNotNull(a.getOriginal());
                            assertNotNull(a.getPublishDateFormat());
                            assertNotNull(a.getOpen());
                            assertNotNull(a.getReadingNumber());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByCategory() {
        try {
            // 分类不存在
            String categoryName = "NoSuchCategory";
            mockMvc.perform(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        assertEquals(CATEGORY_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
                    });
            // 正常查询
            categoryName = "linux";
            mockMvc.perform(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        JSONObject jsonObject = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), jsonObject.getInt(Code));
                        PageInfo pageInfo = (PageInfo) JSONObject.toBean(jsonObject.getJSONObject(Result), PageInfo.class);
                        assertNotEquals(0, pageInfo.getTotal());
                        assertNotEquals(0, pageInfo.getStartRow());
                        assertNotEquals(0, pageInfo.getEndRow());
                        assertEquals(1, pageInfo.getPageNum());
                        assertEquals(10, pageInfo.getPageSize());
                        for (Object arc : pageInfo.getList()) {
                            JSONObject jsonObject1 = JSONObject.fromObject(arc);
                            assertNotEquals(0, jsonObject1.getInt("id"));
                            assertNotNull(jsonObject1.getString("title"));
                            assertNotNull(jsonObject1.getString("summary"));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByTag() {
        try {
            // 分类不存在
            String tagName = "NoSuchTag";
            mockMvc.perform(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        assertEquals(TAG_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
                    });
            // 正常查询
            tagName = "linux";
            mockMvc.perform(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andExpect(status().is(200))
                    .andDo(result -> {
                        JSONObject jsonObject = JSONObject.fromObject(result.getResponse().getContentAsString());
                        assertEquals(SUCCESS.getCode(), jsonObject.getInt(Code));
                        PageInfo pageInfo = (PageInfo) JSONObject.toBean(jsonObject.getJSONObject(Result), PageInfo.class);
                        assertNotEquals(0, pageInfo.getTotal());
                        assertNotEquals(0, pageInfo.getStartRow());
                        assertNotEquals(0, pageInfo.getEndRow());
                        assertEquals(1, pageInfo.getPageNum());
                        assertEquals(10, pageInfo.getPageSize());

                        for (Object arc : pageInfo.getList()) {
                            JSONObject jsonObject1 = JSONObject.fromObject(arc);
                            assertNotEquals(0, jsonObject1.getInt("id"));
                            assertNotNull(jsonObject1.getString("title"));
                            assertNotNull(jsonObject1.getString("summary"));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
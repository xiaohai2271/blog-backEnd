package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.CategoryModel;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.mapper.ArticleMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class ArticleControllerTest extends BaseTest {
    @Autowired
    ArticleMapper articleMapper;
    private static final TypeReference<?> ARTICLE_MODEL_TYPE = new TypeReference<Response<ArticleModel>>() {
    };
    private static final TypeReference<?> ARTICLE_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<ArticleModel>>>() {
    };

    @Test
    public void create() {
        ArticleReq articleReq = new ArticleReq();
        // 应该正常通过
        articleReq.setTitle("test-" + randomStr());
        articleReq.setMdContent("# test title");
        articleReq.setCategory("随笔");
        String[] tagList = {"tag", "category"};
        articleReq.setTags(tagList);
        articleReq.setOpen(true);
        articleReq.setType(true);
        articleReq.setUrl("http://xxxx.com");
        MockHttpServletRequestBuilder post = post("/admin/article/create");

        try {
            getMockData(post, adminLogin(), articleReq).andDo(result -> {
                Response<ArticleModel> response = getResponse(result, ARTICLE_MODEL_TYPE);
                assertEquals(SUCCESS.getCode(), response.getCode());
                assertNotNull(response.getResult());
                ArticleModel articleModel = response.getResult();
                assertNotNull(articleModel.getId());
                assertNotNull(articleModel.getTitle());
                assertNotNull(articleModel.getSummary());
                assertNotNull(articleModel.getOriginal());
                assertNotNull(articleModel.getTags());
                assertNotNull(articleModel.getCategory());
                assertNotNull(articleModel.getPublishDateFormat());
                assertNotNull(articleModel.getMdContent());
                assertNotNull(articleModel.getPreArticle());
                assertNull(articleModel.getNextArticle());
                assertNotNull(articleModel.getOpen());
                assertNotNull(articleModel.getReadingNumber());
                assertNotNull(articleModel.getAuthor());
                assertNotNull(articleModel.getUrl());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        Article article;
        do {
            article = articleMapper.getLastestArticle();
            create();
        } while (article.isDeleted());
        assertFalse(article.isDeleted());
        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/admin/article/del?articleID=" + article.getId());
        try {
            Article finalArticle = article;
            getMockData(delete, adminLogin()).andDo(result -> {
                Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
                assertEquals(SUCCESS.getCode(), response.getCode());
                // 断言删除成功
                assertTrue(response.getResult());
                assertTrue(articleMapper.isDeletedById(finalArticle.getId()));
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
        String[] tagList = {"test", tag1, tag2};
        articleReq.setTags(tagList);
        articleReq.setTitle("test-" + article.getTitle());
        try {
            getMockData(put("/admin/article/update"), adminLogin(), articleReq).andDo(result -> {
                Response<ArticleModel> response = getResponse(result, ARTICLE_MODEL_TYPE);
                assertEquals(SUCCESS.getCode(), response.getCode());
                ArticleModel a = response.getResult();
                assertEquals(articleReq.getCategory(), a.getCategory());
                assertEquals(articleReq.getUrl(), a.getUrl());
                assertEquals(articleReq.getMdContent(), a.getMdContent());
                assertEquals(articleReq.getTitle(), a.getTitle());
                assertEquals(articleReq.getType(), a.getOriginal());
                // Tag
                List<Tag> asList = a.getTags();
                assertEquals(3, asList.size());
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
            getMockData(MockMvcRequestBuilders.get("/article/articleID/" + articleID));
            getMockData(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=true"));

            // 文章不存在
            getMockData(MockMvcRequestBuilders.get("/article/articleID/-1"))
                    .andDo(result -> assertEquals(ARTICLE_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode()));

            // 正常情况
            getMockData(MockMvcRequestBuilders.get("/article/articleID/" + articleID + "?update=false")).andDo(result -> {
                Response<ArticleModel> response = getResponse(result, ARTICLE_MODEL_TYPE);
                // 断言获取数据成功
                assertEquals(SUCCESS.getCode(), response.getCode());
                assertNotNull(response.getResult());

                ArticleModel a = response.getResult();
                assertNotNull(a.getTitle());
                assertNotNull(a.getId());
                assertNotNull(a.getSummary());
                assertNotNull(a.getMdContent());
                assertNotNull(a.getUrl());
                assertNotNull(a.getUpdateDateFormat());
                assertTrue(a.getPreArticle() != null || a.getNextArticle() != null);
                assertNotNull(a.getReadingNumber());
                assertNotNull(a.getOriginal());
                assertNotNull(a.getPublishDateFormat());
                assertNotNull(a.getCategory());
                assertNotNull(a.getTags());
                assertNotNull(a.getAuthor());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void articles() {
        try {
            // 测试不带参数访问
            getMockData(MockMvcRequestBuilders.get("/articles"));
            getMockData(MockMvcRequestBuilders.get("/articles?page=1&count=5")).andDo(result -> {
                Response<PageData<ArticleModel>> response = getResponse(result, ARTICLE_MODEL_PAGE_TYPE);
                // 断言获取数据成功
                assertEquals(SUCCESS.getCode(), response.getCode());
                // 结果集非空
                assertNotNull(response.getResult());
                // 判断pageInfo是否包装完全
                PageData<ArticleModel> pageData = response.getResult();
                assertNotEquals(0, pageData.getTotal());
                assertEquals(1, pageData.getPageNum());
                assertEquals(5, pageData.getPageSize());
                // 内容完整
                for (ArticleModel a : pageData.getList()) {
                    assertNotNull(a.getTitle());
                    assertNotNull(a.getId());
                    assertNotNull(a.getSummary());
                    assertNotNull(a.getOriginal());
                    assertNotNull(a.getPublishDateFormat());
                    assertNotNull(a.getCategory());
                    assertNotNull(a.getTags());
                    assertNotNull(a.getAuthor());
                    assertNull(a.getOpen());
                    assertNull(a.getMdContent());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void adminArticles() {
        try {
            getMockData(get("/admin/articles?page=1&count=10")).andExpect(result ->
                    assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result, STRING_TYPE).getCode())
            );

            // User权限登陆
            getMockData(get("/admin/articles?page=1&count=10"), userLogin()).andDo(result ->
                    assertEquals(PERMISSION_ERROR.getCode(), getResponse(result, STRING_TYPE).getCode())
            );
            for (int i = 0; i < 2; i++) {
                // admin权限登陆
                int finalI = i;
                getMockData(get("/admin/articles?page=1&count=10&deleted=" + (i == 1)), adminLogin()).andDo(result -> {
                    Response<PageData<ArticleModel>> response = getResponse(result, ARTICLE_MODEL_PAGE_TYPE);
                    assertEquals(SUCCESS.getCode(), response.getCode());
                    assertNotNull(response.getResult());
                    // 判断pageInfo是否包装完全
                    PageData<ArticleModel> pageData = response.getResult();
                    assertNotEquals(0, pageData.getTotal());
                    assertEquals(1, pageData.getPageNum());
                    assertEquals(10, pageData.getPageSize());
                    // 内容完整
                    for (ArticleModel a : pageData.getList()) {
                        assertNotNull(a.getTitle());
                        assertNotNull(a.getId());
                        assertNotNull(a.getOriginal());
                        assertNotNull(a.getPublishDateFormat());
                        assertNotNull(a.getOpen());
                        assertNotNull(a.getReadingNumber());
                        assertNotNull(a.getLikeCount());
                        assertNotNull(a.getDislikeCount());
                        assertEquals((finalI == 1), a.isDeleted());
                        assertNull(a.getMdContent());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByCategory() {
        try {
            // 分类不存在
            String categoryName = "NoSuchCategory";
            getMockData(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andDo(result -> assertEquals(CATEGORY_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode()));
            // 正常查询
            categoryName = "linux";
            getMockData(MockMvcRequestBuilders.get("/articles/category/" + categoryName + "?page=1&count=10"))
                    .andDo(result -> {
                        Response<PageData<ArticleModel>> response = getResponse(result, ARTICLE_MODEL_PAGE_TYPE);
                        assertEquals(SUCCESS.getCode(), response.getCode());
                        PageData<ArticleModel> pageData = response.getResult();
                        assertNotEquals(0, pageData.getTotal());
                        assertEquals(1, pageData.getPageNum());
                        assertEquals(10, pageData.getPageSize());
                        for (ArticleModel arc : pageData.getList()) {
                            assertNotEquals(0, arc.getId().longValue());
                            assertNotNull(arc.getTitle());
                            assertNotNull(arc.getSummary());
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
            getMockData(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andDo(result -> assertEquals(TAG_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode()));
            // 正常查询
            tagName = "linux";
            getMockData(MockMvcRequestBuilders.get("/articles/tag/" + tagName + "?page=1&count=10"))
                    .andDo(result -> {
                        Response<PageData<ArticleModel>> response = getResponse(result, ARTICLE_MODEL_PAGE_TYPE);
                        assertEquals(SUCCESS.getCode(), response.getCode());
                        PageData<ArticleModel> pageData = response.getResult();
                        assertNotEquals(0, pageData.getTotal());
                        assertEquals(1, pageData.getPageNum());
                        assertEquals(10, pageData.getPageSize());

                        for (ArticleModel arc : pageData.getList()) {
                            assertNotEquals(0, arc.getId().longValue());
                            assertNotNull(arc.getTitle());
                            assertNotNull(arc.getSummary());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
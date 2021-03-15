package cn.celess.blog.service;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.PageData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ArticleServiceTest extends BaseTest {

    @Autowired
    ArticleService articleService;

    @Test
    public void adminArticles() {
        // 测deleted参数传值
        PageData<ArticleModel> pageData = articleService.adminArticles(10, 1, true);
        assertTrue(pageData.getList().stream().allMatch(ArticleModel::isDeleted));
        pageData = articleService.adminArticles(10, 1, false);
        assertFalse(pageData.getList().stream().allMatch(ArticleModel::isDeleted));
        pageData = articleService.adminArticles(10, 1, null);
        assertTrue(pageData.getList().stream().anyMatch(ArticleModel::isDeleted));
        assertTrue(pageData.getList().stream().anyMatch(articleModel -> !articleModel.isDeleted()));
    }

    @Test
    public void retrievePageForOpen() {
        PageData<ArticleModel> articleModelPageData = articleService.retrievePageForOpen(10, 1);
        assertEquals(10, articleModelPageData.getPageSize());
        assertEquals(1, articleModelPageData.getPageNum());
        assertEquals(10, articleModelPageData.getList().size());
        articleModelPageData.getList().forEach(Assert::assertNotNull);
    }
}

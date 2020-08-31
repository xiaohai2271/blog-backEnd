package cn.celess.blog.service;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.PageData;
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
}
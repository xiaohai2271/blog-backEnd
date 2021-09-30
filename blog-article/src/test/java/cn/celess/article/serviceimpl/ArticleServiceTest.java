package cn.celess.article.serviceimpl;

import cn.celess.article.ArticleBaseTest;
import cn.celess.common.entity.vo.ArticleModel;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.mapper.ArticleMapper;
import cn.celess.common.service.ArticleService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class ArticleServiceTest extends ArticleBaseTest {

    @Autowired
    ArticleService articleService;
    @Resource
    ArticleMapper articleMapper;

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

        // 测试open字段
        articleModelPageData.getList().forEach(articleModel -> {
            // 当前文章
            assertTrue(articleMapper.findArticleById(articleModel.getId()).getOpen());
            if (articleModel.getPreArticle() != null) {
                assertTrue(articleMapper.findArticleById(articleModel.getPreArticle().getId()).getOpen());
            }
            if (articleModel.getNextArticle() != null) {
                assertTrue(articleMapper.findArticleById(articleModel.getNextArticle().getId()).getOpen());
            }
        });
    }
}

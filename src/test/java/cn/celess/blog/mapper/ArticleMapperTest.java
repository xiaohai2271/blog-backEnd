package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ArticleMapperTest extends BaseTest {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    ArticleTagMapper articleTagMapper;

    @Test
    public void insert() {

        Article article = generateArticle().getArticle();
        assertNotNull(article.getId());
    }


    @Test
    public void delete() {
        Article article = generateArticle().getArticle();

        assertFalse(articleMapper.isDeletedById(article.getId()));
        assertEquals(1, articleMapper.delete(article.getId()));
        assertTrue(articleMapper.isDeletedById(article.getId()));


    }

    @Test
    public void update() {
        Article article = generateArticle().getArticle();
        String randomText = UUID.randomUUID().toString();

        // 此字段不会通过insert被写入数据库而是使用插入数据的默认值   数据库中该字段默认为true
        article.setOpen(true);

        article.setTitle("test update " + randomText);
        article.setMdContent("test update ");
        article.setSummary("test update ");
        article.setType(false);
        article.setUrl("https://www.celess.cn");
        article.getCategory().setId(2L);
        article.setOpen(!article.getOpen());
        articleMapper.update(article);

        Article articleById = articleMapper.findArticleById(article.getId());
        assertEquals(article.getTitle(), articleById.getTitle());
        assertEquals(article.getMdContent(), articleById.getMdContent());
        assertEquals(article.getSummary(), articleById.getSummary());
        assertEquals(article.getType(), articleById.getType());
        assertEquals(article.getCategory().getId(), articleById.getCategory().getId());
        assertEquals(article.getOpen(), articleById.getOpen());
        assertNotNull(articleById.getUpdateDate());
    }

    @Test
    public void selectAll() {
        List<Article> list = articleMapper.findAll();
        assertNotEquals(0, list.size());
        list.forEach(article -> {
            assertNotEquals(0, article.getTags().size());
            assertNotNull(article.getId());
            assertNotNull(article.getTitle());
            assertNotNull(article.getSummary());
            assertNotNull(article.getMdContent());
            assertNotNull(article.getType());
            if (!article.getType()) {
                assertNotNull(article.getUrl());
            }
            assertNotNull(article.getReadingNumber());
            assertNotNull(article.getLikeCount());
            assertNotNull(article.getDislikeCount());
            assertNotNull(article.getPublishDate());
            assertNotNull(article.getOpen());
            assertNotNull(article.getCategory());
            assertNotNull(article.getUser());
        });
    }


    @Test
    public void updateReadCount() {
        Article article = generateArticle().getArticle();
        Article articleById = articleMapper.findArticleById(article.getId());
        assertEquals(Long.valueOf(0), articleById.getReadingNumber());
        articleMapper.updateReadingNumber(articleById.getId());
        articleById = articleMapper.findArticleById(article.getId());
        assertEquals(Long.valueOf(1), articleById.getReadingNumber());
    }


    @Test
    public void getLastestArticle() {
        Article article = generateArticle().getArticle();
        Article lastestArticle = articleMapper.getLastestArticle();
        assertNotNull(lastestArticle);
        assertEquals(article.getId(), lastestArticle.getId());
        assertNotNull(lastestArticle.getCategory());
        assertNotEquals(0, lastestArticle.getTags().size());
    }


    private ArticleTag generateArticle() {
        String randomText = UUID.randomUUID().toString();

        Article article = new Article();
        Category category = new Category();
        category.setId(1L);
        article.setCategory(category);
        article.setTitle(" unity test for  article " + randomText);
        article.setMdContent("# unity test for  article");
        article.setSummary("unity test for  article");

        Tag tag =  tagMapper.findTagByName("随笔");
        User user = new User();
        user.setId(1L);
        article.setUser(user);
        article.setType(true);

        articleMapper.insert(article);

        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticle(article);
        articleTag.setTag(tag);
        articleTagMapper.insert(articleTag);

        return articleTag;
    }
}
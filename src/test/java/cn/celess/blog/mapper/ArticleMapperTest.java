package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        String randomText = randomStr();

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

    @Test
    public void findArticleById() {
        Article article = generateArticle().getArticle();
        Article articleById = articleMapper.findArticleById(article.getId());
        assertNotNull(articleById);
        assertEquals(article.getId(), articleById.getId());
    }

    @Test
    public void existsByTitle() {
        Article article = generateArticle().getArticle();
        assertTrue(articleMapper.existsByTitle(article.getTitle()));
        assertFalse(articleMapper.existsByTitle(randomStr()));
    }

    @Test
    public void isDeletedById() {
        Article article = generateArticle().getArticle();
        assertFalse(articleMapper.isDeletedById(article.getId()));
        articleMapper.delete(article.getId());
        assertTrue(articleMapper.isDeletedById(article.getId()));

    }

    @Test
    public void findAllByAuthorId() {
        List<Article> allByAuthorId = articleMapper.findAllByAuthorId(1);
        assertNotEquals(0, allByAuthorId.size());
    }

    @Test
    public void findAllByOpen() {
        List<Article> allByOpen = articleMapper.findAllByOpen(true);
        assertNotEquals(0, allByOpen);

        Article article = generateArticle().getArticle();
        article.setOpen(false);

        articleMapper.update(article);

        List<Article> privateArticles = articleMapper.findAllByOpen(false);
        assertTrue(privateArticles.size() > 0);
    }

    @Test
    public void getTitleById() {
        Article article = generateArticle().getArticle();

        assertEquals(article.getTitle(), articleMapper.getTitleById(article.getId()));
    }

    @Test
    public void findAllByCategoryId() {
        List<Article> allByCategoryId = articleMapper.findAllByCategoryId(1);
        assertNotEquals(0, allByCategoryId.size());
    }

    @Test
    public void findAllByCategoryIdAndOpen() {
        List<Article> allByCategoryId = articleMapper.findAllByCategoryIdAndOpen(1);
        assertNotEquals(0, allByCategoryId.size());
        allByCategoryId.forEach(article -> assertTrue(article.getOpen()));
    }

    @Test
    public void findAll() {
        List<Article> allByCategoryId = articleMapper.findAll();
        assertNotEquals(0, allByCategoryId.size());
    }

    @Test
    public void count() {
        assertNotEquals(0, articleMapper.count());
    }

    @Test
    public void getPreArticle() {
        ArticleTag articleTag = generateArticle();
        Article preArticle = articleMapper.getPreArticle(articleTag.getArticle().getId());
        assertNotNull(preArticle);
        assertTrue(preArticle.getId() < articleTag.getArticle().getId());
    }

    @Test
    public void getNextArticle() {
        Article getNextArticle = articleMapper.getNextArticle(3L);
        assertNotNull(getNextArticle);
        assertTrue(getNextArticle.getId() > 3L);
    }

    private ArticleTag generateArticle() {
        String randomText = randomStr();

        Article article = new Article();
        Category category = new Category();
        category.setId(1L);
        article.setCategory(category);
        article.setTitle(" unity test for  article " + randomText);
        article.setMdContent("# unity test for  article");
        article.setSummary("unity test for  article");

        Tag tag = tagMapper.findTagByName("随笔");
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
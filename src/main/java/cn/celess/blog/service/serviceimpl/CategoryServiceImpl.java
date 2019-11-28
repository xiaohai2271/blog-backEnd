package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Category;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.CategoryMapper;
import cn.celess.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:43
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public Category create(String name) {
        if (categoryMapper.existsByName(name)) {
            throw new MyException(ResponseEnum.CATEGORY_HAS_EXIST);
        }
        Category category = new Category();
        category.setName(name);
        category.setArticles("");
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category create(Category category) {
        if (category == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public boolean delete(long id) {
        Category category = categoryMapper.findCategoryById(id);

        if (category == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        String[] articleArray = category.getArticles().split(",");
        for (int i = 0; i < articleArray.length; i++) {
            if (articleArray[i] == null || "".equals(articleArray[i])) {
                continue;
            }
            long articleId = Long.parseLong(articleArray[i]);
            Article article = articleMapper.findArticleById(articleId);
            if (article == null) {
                continue;
            }
            article.setCategoryId(-1L);
            //一个 文章只对应一个分类，分类不存在则文章默认不可见
            article.setOpen(false);
            articleMapper.update(article);
        }
        return categoryMapper.delete(id) == 1;
    }


    @Override
    public Category update(Long id, String name) {
        if (id == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "id不可为空");
        }
        Category category = categoryMapper.findCategoryById(id);
        category.setName(name);
        categoryMapper.update(category);
        return category;
    }

    @Override
    public List<Category> retrievePage() {
        return categoryMapper.findAll();
    }
}

package cn.celess.categorytag.serviceimpl;

import cn.celess.common.enmu.ResponseEnum;
import cn.celess.common.entity.Article;
import cn.celess.common.entity.Category;
import cn.celess.common.entity.vo.ArticleModel;
import cn.celess.common.entity.vo.CategoryModel;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.exception.MyException;
import cn.celess.common.mapper.ArticleMapper;
import cn.celess.common.mapper.CategoryMapper;
import cn.celess.common.service.CategoryService;
import cn.celess.common.util.ModalTrans;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    public CategoryModel create(String name) {
        if (categoryMapper.existsByName(name)) {
            throw new MyException(ResponseEnum.CATEGORY_HAS_EXIST);
        }
        Category category = new Category();
        category.setName(name);
        categoryMapper.insert(category);
        return ModalTrans.category(category);
    }

    @Override
    public boolean delete(long id) {
        Category category = categoryMapper.findCategoryById(id);
        if (category == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        return categoryMapper.delete(id) == 1;
    }

    @Override
    public CategoryModel update(Long id, String name) {
        if (id == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "id不可为空");
        }
        Category category = categoryMapper.findCategoryById(id);
        category.setName(name);
        categoryMapper.update(category);
        return ModalTrans.category(category);
    }

    @Override
    public PageData<CategoryModel> retrievePage(int page, int count) {
        PageHelper.startPage(page, count);
        List<Category> all = categoryMapper.findAll();
        // 遍历没一个category
        List<CategoryModel> modelList = all
                .stream()
                .map(ModalTrans::category)
                .peek(categoryModel -> {
                    // 根据category去查article，并赋值给categoryModel
                    List<Article> allByCategoryId = articleMapper.findAllByCategoryId(categoryModel.getId());
                    List<ArticleModel> articleModelList = allByCategoryId
                            .stream()
                            .map(article -> ModalTrans.article(article, true))
                            .peek(articleModel -> {
                                // 去除不必要的字段
                                articleModel.setPreArticle(null);
                                articleModel.setNextArticle(null);
                                articleModel.setTags(null);
                            })
                            .collect(Collectors.toList());
                    categoryModel.setArticles(articleModelList);
                }).collect(Collectors.toList());
        return new PageData<>(new PageInfo<>(all), modelList);
    }
}

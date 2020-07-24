package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Category;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.CategoryModel;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.mapper.CategoryMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class CategoryControllerTest extends BaseTest {

    @Autowired
    CategoryMapper categoryMapper;
    private static final TypeReference<?> CATEGORY_MODEL_TYPE = new TypeReference<Response<CategoryModel>>() {
    };
    private static final TypeReference<?> CATEGORY_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<CategoryModel>>>() {
    };

    @Test
    public void addOne() throws Exception {
        String categoryName = randomStr(4);
        getMockData(post("/admin/category/create?name=" + categoryName), adminLogin()).andDo(result -> {
            Response<CategoryModel> response = getResponse(result, CATEGORY_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CategoryModel category = response.getResult();
            assertEquals(categoryName, category.getName());
            assertNotNull(category.getId());
            assertNotEquals(0, category.getArticles());
        });
    }

    @Test
    public void deleteOne() throws Exception {
        Category category = categoryMapper.getLastestCategory();
        getMockData(delete("/admin/category/del?id=" + category.getId()), adminLogin()).andDo(result -> {
            Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertTrue(response.getResult());
        });
    }

    @Test
    public void updateOne() throws Exception {
        Category category = categoryMapper.getLastestCategory();
        String name = randomStr(4);
        getMockData(put("/admin/category/update?id=" + category.getId() + "&name=" + name), adminLogin()).andDo(result -> {
            //            Response<CategoryModel> response = mapper.readValue(result.getResponse().getContentAsString(), new ResponseType<Response<CategoryModel>>());
            Response<CategoryModel> response = getResponse(result, CATEGORY_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            CategoryModel c = response.getResult();
            assertEquals(name, c.getName());
            assertNotEquals(0, c.getArticles());
            assertNotNull(c.getId());
        });
    }

    @Test
    public void getPage() throws Exception {
        getMockData(get("/categories")).andDo(result -> {
            Response<PageData<CategoryModel>> response = getResponse(result, CATEGORY_MODEL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            response.getResult().getList().forEach(c -> {
                assertNotNull(c.getName());
                assertNotNull(c.getId());
                assertNotEquals(0, c.getArticles());
            });
        });

    }
}
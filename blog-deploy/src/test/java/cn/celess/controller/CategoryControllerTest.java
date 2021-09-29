package cn.celess.controller;

import cn.celess.BaseTest;
import cn.celess.common.entity.Category;
import cn.celess.common.entity.Response;
import cn.celess.common.entity.vo.CategoryModel;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.mapper.CategoryMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.celess.common.enmu.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
            assertNull(category.getArticles());
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
            assertNull(c.getArticles());
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
                assertNotNull(c.getArticles());
            });
        });

    }
}
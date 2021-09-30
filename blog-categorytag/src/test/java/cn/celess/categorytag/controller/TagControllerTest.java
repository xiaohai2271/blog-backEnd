package cn.celess.categorytag.controller;


import cn.celess.categorytag.CategoryTagBaseTest;
import cn.celess.common.entity.Response;
import cn.celess.common.entity.Tag;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.TagModel;
import cn.celess.common.mapper.TagMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static cn.celess.common.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TagControllerTest extends CategoryTagBaseTest {
    @Autowired
    TagMapper tagMapper;
    private static final TypeReference<?> TAG_MODEL_TYPE = new TypeReference<Response<TagModel>>() {

    };
    private static final TypeReference<?> TAG_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<TagModel>>>() {
    };
    private static final TypeReference<?> TAG_NAC_LIST_TYPE = new TypeReference<Response<List<Map<String, Object>>>>() {
    };

    @Test
    public void addOne() throws Exception {
        String name = randomStr(4);
        getMockData(post("/admin/tag/create?name=" + name)).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), getResponse(result, STRING_TYPE).getCode()));
        getMockData(post("/admin/tag/create?name=" + name), userLogin()).andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), getResponse(result, STRING_TYPE).getCode()));
        getMockData(post("/admin/tag/create?name=" + name), adminLogin()).andDo(result -> {
            Response<TagModel> response = getResponse(result, TAG_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            TagModel tag = response.getResult();
            assertNotNull(tag.getId());
            assertEquals(name, tag.getName());
        });


    }

    @Test
    public void delOne() throws Exception {
        Tag lastestTag = tagMapper.getLastestTag();
        assertNotNull(lastestTag.getId());
        getMockData(delete("/admin/tag/del?id=" + lastestTag.getId()), adminLogin()).andDo(result -> {
            Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertTrue(response.getResult());
        });
        long id = lastestTag.getId() * 2;
        getMockData(delete("/admin/tag/del?id=" + id), adminLogin())
                .andDo(result -> assertEquals(TAG_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode()));

    }

    @Test
    public void updateOne() throws Exception {
        Tag tag = tagMapper.getLastestTag();
        assertNotNull(tag.getId());
        String name = randomStr(4);
        getMockData(put("/admin/tag/update?id=" + tag.getId() + "&name=" + name), adminLogin()).andDo(result -> {
            Response<TagModel> response = getResponse(result, TAG_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            TagModel t = response.getResult();
            assertEquals(name, t.getName());
            assertEquals(tag.getId(), t.getId());
        });

    }

    @Test
    public void getPage() throws Exception {
        getMockData(get("/tags?page=1&count=5")).andDo(result -> {
            Response<PageData<TagModel>> response = getResponse(result, TAG_MODEL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            // 结果集非空
            assertNotNull(response.getResult());
            // 判断pageInfo是否包装完全
            PageData<TagModel> pageData = response.getResult();
            assertNotEquals(0, pageData.getTotal());
            assertEquals(1, pageData.getPageNum());
            assertEquals(5, pageData.getPageSize());
            // 内容完整
            for (TagModel t : pageData.getList()) {
                assertNotNull(t.getId());
                assertNotNull(t.getName());
            }
        });
    }

    @Test
    public void getTagNameAndCount() throws Exception {
        getMockData(get("/tags/nac")).andDo(result -> {
            Response<List<Map<String, Object>>> response = getResponse(result, TAG_NAC_LIST_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            response.getResult().forEach(o -> {
                assertNotNull(o.get("name"));
                assertNotNull(o.get("size"));
            });
        });
    }
}
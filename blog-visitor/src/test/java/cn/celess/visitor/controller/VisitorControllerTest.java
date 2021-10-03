package cn.celess.visitor.controller;

import cn.celess.common.entity.Response;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.VisitorModel;
import cn.celess.visitor.VisitorBaseTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

import static cn.celess.common.constant.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class VisitorControllerTest extends VisitorBaseTest {
    private final TypeReference<?> VISITOR_PAGE_TYPE = new TypeReference<Response<PageData<VisitorModel>>>() {
    };
    private final TypeReference<?> VISITOR_TYPE = new TypeReference<Response<VisitorModel>>() {
    };

    @Test
    public void getVisitorCount() throws Exception {
        getMockData(get("/visitor/count")).andDo(result -> {
            Response<Object> response = getResponse(result);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
        });
    }

    @Test
    public void page() throws Exception {
        int count = 10;
        int page = 1;
        // 默认显示location
        getMockData(get("/admin/visitor/page?count=" + count + "&page=" + page), adminLogin()).andDo(result -> {
            Response<PageData<VisitorModel>> response = getResponse(result, VISITOR_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PageData<VisitorModel> pageData = response.getResult();
            assertNotEquals(0, pageData.getTotal());
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (VisitorModel v : pageData.getList()) {
                assertNotEquals(0, v.getId());
                assertNotNull(v.getDate());
                assertNotNull(v.getLocation());
            }
        });
    }

    @Test
    public void add() throws Exception {
        getMockData(post("/visit")).andDo(result -> {
            Response<VisitorModel> response = getResponse(result, VISITOR_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            VisitorModel visitorModel = response.getResult();
            assertNotEquals(0, visitorModel.getId());
            assertNotNull(visitorModel.getIp());
        });
    }

    @Test
    public void dayVisitCount() throws Exception {
        getMockData(get("/dayVisitCount")).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result).getCode()));
    }

    // 手动测试
    // @Test
    public void ipLocation() throws Exception {
        String ip = "127.0.0.1";
        getMockData(get("/ip/" + ip)).andDo(result -> {
            Response<Object> response = getResponse(result);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
        });
    }

    @Test
    public void getIp() throws Exception {
        getMockData(get("/ip")).andDo(result -> {
            Response<String> response = getResponse(result, STRING_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertEquals("127.0.0.1", response.getResult());
        });
    }
}
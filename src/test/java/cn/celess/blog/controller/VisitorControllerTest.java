package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.VisitorModel;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class VisitorControllerTest extends BaseTest {

    @Test
    public void getVisitorCount() throws Exception {
        mockMvc.perform(get("/visitor/count")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.containsKey(Result));
        });
    }

    @Test
    public void page() throws Exception {
        int count = 10;
        int page = 1;
        mockMvc.perform(get("/admin/visitor/page?count=" + count + "&page=" + page).header("Authorization", adminLogin()))
                .andDo(result -> {
                    JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
                    assertEquals(SUCCESS.getCode(), object.getInt(Code));
                    JSONObject resultJson = JSONObject.fromObject(object.getJSONObject(Result));
                    PageData<VisitorModel> pageData = (PageData<VisitorModel>) JSONObject.toBean(resultJson, PageData.class);
                    assertNotEquals(0, pageData.getTotal());
                    assertEquals(1, pageData.getPageNum());
                    assertEquals(10, pageData.getPageSize());
                    for (Object ver : pageData.getList()) {
                        VisitorModel v = (VisitorModel) JSONObject.toBean(JSONObject.fromObject(ver), VisitorModel.class);
                        assertNotEquals(0, v.getId());
                        assertNotNull(v.getDate());
                    }
                });
    }

    @Test
    public void add() throws Exception {
        mockMvc.perform(post("/visit")).andDo(MockMvcResultHandlers.print()).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            VisitorModel visitorModel = (VisitorModel) JSONObject.toBean(object.getJSONObject(Result), VisitorModel.class);
            assertNotEquals(0, visitorModel.getId());
            assertNotNull(visitorModel.getIp());
        });
    }

    @Test
    public void dayVisitCount() throws Exception {
        mockMvc.perform(get("/dayVisitCount")).andDo(MockMvcResultHandlers.print()).andDo(result ->
                assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
        );
    }

    // 手动测试
    // @Test
    public void ipLocation() throws Exception {
        String ip = "127.0.0.1";
        mockMvc.perform(get("/ip/" + ip)).andDo(MockMvcResultHandlers.print()).andDo(result -> {
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
            assertTrue(JSONObject.fromObject(result.getResponse().getContentAsString()).containsKey(Result));
        });
    }

    @Test
    public void getIp() throws Exception {
        mockMvc.perform(get("/ip")).andDo(MockMvcResultHandlers.print()).andDo(result -> {
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
            assertEquals("127.0.0.1", JSONObject.fromObject(result.getResponse().getContentAsString()).getString(Result));
        });
    }
}
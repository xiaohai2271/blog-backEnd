package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.WebUpdate;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.WebUpdateModel;
import cn.celess.blog.mapper.WebUpdateInfoMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class WebUpdateInfoControllerTest extends BaseTest {
    @Autowired
    WebUpdateInfoMapper mapper;

    @Test
    public void create() throws Exception {
        String info = UUID.randomUUID().toString();
        mockMvc.perform(post("/admin/webUpdate/create?info=" + info).header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.containsKey(Result));
            WebUpdateModel webUpdateModel = (WebUpdateModel) JSONObject.toBean(object.getJSONObject(Result), WebUpdateModel.class);
            assertEquals(info, webUpdateModel.getInfo());
            assertNotNull(webUpdateModel.getTime());
            assertNotEquals(0, webUpdateModel.getId());
        });
    }

    @Test
    public void del() throws Exception {
        // 新增数据
        WebUpdate webUpdate = new WebUpdate();
        webUpdate.setUpdateInfo(UUID.randomUUID().toString());
        webUpdate.setUpdateTime(new Date());
        mapper.insert(webUpdate);
        // 接口测试
        List<WebUpdate> updateList = mapper.findAll();
        WebUpdate update = updateList.get(updateList.size() - 1);
        assertNotEquals(0, update.getId());

        long id = update.getId();
        mockMvc.perform(delete("/admin/webUpdate/del/" + id).header("Authorization", adminLogin())).andDo(result -> {
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
            assertTrue(JSONObject.fromObject(result.getResponse().getContentAsString()).getBoolean(Result));
        });
        do {
            id += 2;
        } while (mapper.existsById(id));
        System.out.println("准备删除ID=" + id + "的不存在记录");
        mockMvc.perform(delete("/admin/webUpdate/del/" + id).header("Authorization", adminLogin())).andDo(result ->
                assertEquals(DATA_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
        );
    }

    @Test
    public void update() throws Exception {
        // 新增数据
        WebUpdate webUpdate = new WebUpdate();
        webUpdate.setUpdateInfo(UUID.randomUUID().toString());
        webUpdate.setUpdateTime(new Date());
        mapper.insert(webUpdate);
        List<WebUpdate> all = mapper.findAll();
        WebUpdate update = all.get(all.size() - 1);
        assertNotEquals(0, update.getId());
        assertNotNull(update.getUpdateInfo());
        String info = UUID.randomUUID().toString();
        mockMvc.perform(put("/admin/webUpdate/update?id=" + update.getId() + "&info=" + info).header("Authorization", adminLogin())).andDo(result -> {
            List<WebUpdate> list = mapper.findAll();
            WebUpdate up = list.get(list.size() - 1);
            assertEquals(update.getId(), up.getId());
            assertEquals(update.getUpdateTime(), up.getUpdateTime());
            assertEquals(info, up.getUpdateInfo());
        });
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/webUpdate")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            JSONArray jsonArray = object.getJSONArray(Result);
            jsonArray.forEach(o -> {
                WebUpdateModel webUpdate = (WebUpdateModel) JSONObject.toBean(JSONObject.fromObject(o), WebUpdateModel.class);
                assertNotEquals(0, webUpdate.getId());
                assertNotNull(webUpdate.getTime());
                assertNotNull(webUpdate.getInfo());
            });
        });
    }

    @Test
    public void page() throws Exception {
        mockMvc.perform(get("/webUpdate/pages?page=1&count=10")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(object.getJSONObject(Result));
            PageData<WebUpdateModel> pageData = (PageData<WebUpdateModel>) JSONObject.toBean(object.getJSONObject(Result), PageData.class);
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (Object o : pageData.getList()) {
                WebUpdateModel model = (WebUpdateModel) JSONObject.toBean(JSONObject.fromObject(o), WebUpdateModel.class);
                assertNotEquals(0, model.getId());
                assertNotNull(model.getTime());
                assertNotNull(model.getInfo());
            }
        });
    }

    @Test
    public void lastestUpdateTime() throws Exception {
        mockMvc.perform(get("/lastestUpdate")).andDo(result ->
                assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
    }
}
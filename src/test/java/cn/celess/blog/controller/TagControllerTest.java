package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.entity.model.TagModel;
import cn.celess.blog.mapper.TagMapper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static cn.celess.blog.enmu.ResponseEnum.*;

public class TagControllerTest extends BaseTest {
    @Autowired
    TagMapper tagMapper;

    @Test
    public void addOne() throws Exception {
        String name = UUID.randomUUID().toString().substring(0, 4);
        mockMvc.perform(post("/admin/tag/create?name=" + name)).andDo(result -> assertEquals(HAVE_NOT_LOG_IN.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(post("/admin/tag/create?name=" + name).header("authorization", userLogin())).andDo(result -> assertEquals(PERMISSION_ERROR.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
        mockMvc.perform(post("/admin/tag/create?name=" + name).header("authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            JSONObject resJson = object.getJSONObject(Result);
            TagModel tag = (TagModel) JSONObject.toBean(resJson, TagModel.class);
            assertNotNull(tag.getId());
            assertEquals(name, tag.getName());
        });


    }

    @Test
    public void delOne() throws Exception {
        Tag lastestTag = tagMapper.getLastestTag();
        assertNotNull(lastestTag.getId());
        String token = adminLogin();
        mockMvc.perform(delete("/admin/tag/del?id=" + lastestTag.getId()).header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.getBoolean(Result));
        });
        long id = lastestTag.getId() * 2;
        mockMvc.perform(delete("/admin/tag/del?id=" + id).header("Authorization", token)).andDo(result ->
                assertEquals(TAG_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
        );

    }

    @Test
    public void updateOne() throws Exception {
        Tag tag = tagMapper.getLastestTag();
        assertNotNull(tag.getId());
        String name = UUID.randomUUID().toString().substring(0, 4);
        mockMvc.perform(put("/admin/tag/update?id=" + tag.getId() + "&name=" + name).header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(object.getJSONObject(Result));
            TagModel t = (TagModel) JSONObject.toBean(object.getJSONObject(Result), TagModel.class);
            assertEquals(name, t.getName());
            StringBuilder s = new StringBuilder();
            t.getArticles().forEach(e -> s.append(e).append(","));
            assertEquals(tag.getArticles(), s.toString());
            assertEquals(tag.getId(), t.getId());
        });

    }

    @Test
    public void retrieveOneById() throws Exception {
        Tag tag = tagMapper.getLastestTag();
        assertNotNull(tag.getId());
        mockMvc.perform(get("/tag/id/" + tag.getId())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(object.getJSONObject(Result));
            TagModel t = (TagModel) JSONObject.toBean(object.getJSONObject(Result), TagModel.class);
            assertEquals(tag.getId(), t.getId());
            assertNotNull(t.getName());
        });
    }

    @Test
    public void retrieveOneByName() throws Exception {
        Tag tag = tagMapper.getLastestTag();
        assertNotNull(tag.getName());
        mockMvc.perform(get("/tag/name/" + tag.getName())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertNotNull(object.getJSONObject(Result));
            TagModel t = (TagModel) JSONObject.toBean(object.getJSONObject(Result), TagModel.class);
            assertEquals(tag.getName(), t.getName());
            assertNotNull(t.getId());
        });
    }

    @Test
    public void getPage() throws Exception {
        mockMvc.perform(get("/tags?page=1&count=5"))
                .andExpect(status().is(200))
                .andDo(result -> {
                    JSONObject articlesJSON = JSONObject.fromObject(result.getResponse().getContentAsString());
                    // 断言获取数据成功
                    assertEquals(SUCCESS.getCode(), articlesJSON.getInt(Code));
                    // 结果集非空
                    assertNotNull(articlesJSON.getJSONObject(Result));
                    // 判断pageInfo是否包装完全
                    JSONObject resultJson = JSONObject.fromObject(articlesJSON.getJSONObject(Result));
                    PageInfo pageInfo = (PageInfo) JSONObject.toBean(resultJson, PageInfo.class);
                    assertNotEquals(0, pageInfo.getTotal());
                    assertNotEquals(0, pageInfo.getStartRow());
                    assertNotEquals(0, pageInfo.getEndRow());
                    assertEquals(1, pageInfo.getPageNum());
                    assertEquals(5, pageInfo.getPageSize());
                    // 内容完整
                    for (Object tag : pageInfo.getList()) {
                        TagModel t = (TagModel) JSONObject.toBean(JSONObject.fromObject(tag), TagModel.class);
                        assertNotNull(t.getId());
                        assertNotNull(t.getName());
                    }
                });
    }

    @Test
    public void getTagNameAndCount() throws Exception {
        mockMvc.perform(get("/tags/nac")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            JSONArray jsonArray = object.getJSONArray(Result);
            assertNotNull(jsonArray);
            jsonArray.forEach(o -> {
                JSONObject json = JSONObject.fromObject(o);
                assertTrue(json.containsKey("size"));
                assertTrue(json.containsKey("name"));
            });
        });
    }
}
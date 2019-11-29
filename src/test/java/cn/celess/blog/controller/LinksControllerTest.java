package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.mapper.PartnerMapper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class LinksControllerTest extends BaseTest {

    @Autowired
    PartnerMapper mapper;

    @Test
    public void create() throws Exception {
        LinkReq linkReq = new LinkReq();
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setOpen(false);
        linkReq.setUrl("https://example.com");
        String token = adminLogin();
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertNotNull(site.getId());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });

        // https/http
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setOpen(false);
        linkReq.setUrl("example.com");
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType("application/json")
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertEquals("http://example.com", site.getUrl());
        });

        // 测试已存在的数据
        mockMvc.perform(
                post("/admin/links/create")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .header("Authorization", token)
                        .contentType("application/json")
        ).andDo(result -> assertEquals(DATA_HAS_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code)));
    }

    @Test
    public void del() throws Exception {
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(UUID.randomUUID().toString().substring(0, 4));
        partnerSite.setOpen(true);
        partnerSite.setUrl("https://www.celess.cn");
        mapper.insert(partnerSite);
        PartnerSite lastest = mapper.getLastest();
        assertNotNull(lastest.getId());
        String token = adminLogin();
        mockMvc.perform(delete("/admin/links/del/" + lastest.getId()).header("Authorization", token)).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            assertTrue(object.getBoolean(Result));
        });
        long id = lastest.getId();
        do {
            id += 1;
        } while (mapper.existsById(id));
        System.out.println("删除ID=" + id + "的数据");
        mockMvc.perform(delete("/admin/links/del/" + id).header("Authorization", token)).andDo(result ->
                assertEquals(DATA_NOT_EXIST.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code))
        );
    }

    @Test
    public void update() throws Exception {
        // 增数据
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(UUID.randomUUID().toString().substring(0, 4));
        partnerSite.setOpen(true);
        partnerSite.setUrl("https://www.celess.cn");
        mapper.insert(partnerSite);
        // 查数据
        PartnerSite lastest = mapper.getLastest();
        assertNotNull(lastest.getId());
        String token = adminLogin();
        // 构建请求
        LinkReq linkReq = new LinkReq();
        linkReq.setUrl(lastest.getUrl());
        linkReq.setOpen(!lastest.getOpen());
        linkReq.setName(UUID.randomUUID().toString().substring(0, 4));
        linkReq.setId(lastest.getId());
        mockMvc.perform(
                put("/admin/links/update")
                        .content(JSONObject.fromObject(linkReq).toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
        ).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PartnerSite site = (PartnerSite) JSONObject.toBean(object.getJSONObject(Result), PartnerSite.class);
            assertNotNull(site.getId());
            assertEquals(linkReq.getId(), site.getId().longValue());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });
    }

    @Test
    public void allForOpen() throws Exception {
        mockMvc.perform(get("/links")).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            object.getJSONArray(Result).forEach(o -> {
                PartnerSite site = (PartnerSite) JSONObject.toBean(JSONObject.fromObject(o), PartnerSite.class);
                assertNotNull(site.getUrl());
                assertNull(site.getOpen());
                assertNotNull(site.getName());
            });
        });
    }

    @Test
    public void all() throws Exception {
        mockMvc.perform(get("/admin/links?page=1&count=10").header("Authorization", adminLogin())).andDo(result -> {
            JSONObject object = JSONObject.fromObject(result.getResponse().getContentAsString());
            assertEquals(SUCCESS.getCode(), object.getInt(Code));
            PageInfo pageInfo = (PageInfo) JSONObject.toBean(object.getJSONObject(Result), PageInfo.class);
            assertNotEquals(0, pageInfo.getStartRow());
            assertNotEquals(0, pageInfo.getEndRow());
            assertEquals(1, pageInfo.getPageNum());
            assertEquals(10, pageInfo.getPageSize());
            pageInfo.getList().forEach(o -> {
                PartnerSite site = (PartnerSite) JSONObject.toBean(JSONObject.fromObject(o), PartnerSite.class);
                assertNotNull(site.getUrl());
                assertNotNull(site.getName());
                assertNotNull(site.getOpen());
            });
        });
    }

    // 手动测试
    // @Test
    public void apply() throws Exception {
        long l = System.currentTimeMillis();
        String url = "https://www.example.com";
        mockMvc.perform(post("/apply?name=小海博客Api测试，请忽略&url=" + url)).andDo(result -> {
            assertEquals(SUCCESS.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
        });
        System.out.println("耗时：" + (System.currentTimeMillis() - l) / 1000 + "s");
        url = "xxxxxxxxxm";
        mockMvc.perform(post("/apply?name=小海博客Api测试，请忽略&url=" + url)).andDo(result -> {
            assertEquals(PARAMETERS_URL_ERROR.getCode(), JSONObject.fromObject(result.getResponse().getContentAsString()).getInt(Code));
        });

    }
}
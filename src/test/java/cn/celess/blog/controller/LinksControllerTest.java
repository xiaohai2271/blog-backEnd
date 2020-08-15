package cn.celess.blog.controller;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.mapper.PartnerMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.celess.blog.enmu.ResponseEnum.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class LinksControllerTest extends BaseTest {

    @Autowired
    PartnerMapper mapper;
    private static final TypeReference<?> LINK_MODEL_TYPE = new TypeReference<Response<PartnerSite>>() {
    };
    private static final TypeReference<?> LINK_MODEL_LIST_TYPE = new TypeReference<Response<List<PartnerSite>>>() {
    };
    private static final TypeReference<?> LINK_MODEL_PAGE_TYPE = new TypeReference<Response<PageData<PartnerSite>>>() {
    };

    @Test
    public void create() throws Exception {
        LinkReq linkReq = new LinkReq();
        linkReq.setName(randomStr(4));
        linkReq.setOpen(false);
        linkReq.setUrl("https://" + randomStr(4) + "example.com");
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertNotNull(site.getId());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });

        // https/http
        linkReq.setName(randomStr(4));
        linkReq.setOpen(false);
        linkReq.setUrl(randomStr(4) + ".example.com");
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertEquals("http://" + linkReq.getUrl(), site.getUrl());
        });

        // 测试已存在的数据
        getMockData(post("/admin/links/create"), adminLogin(), linkReq).andDo(result ->
                assertEquals(DATA_HAS_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode())
        );
    }

    @Test
    public void del() throws Exception {
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(randomStr(4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setUrl("https://" + randomStr(4) + ".celess.cn");
        mapper.insert(partnerSite);
        PartnerSite latest = mapper.getLastest();
        assertNotNull(latest.getId());
        getMockData(delete("/admin/links/del/" + latest.getId()), adminLogin()).andDo(result -> {
            Response<Boolean> response = getResponse(result, BOOLEAN_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertTrue(response.getResult());
        });
        long id = latest.getId();
        do {
            id += 1;
        } while (mapper.existsById(id));
        System.out.println("删除ID=" + id + "的数据");
        getMockData(delete("/admin/links/del/" + id), adminLogin()).andDo(result ->
                assertEquals(DATA_NOT_EXIST.getCode(), getResponse(result, STRING_TYPE).getCode())
        );
    }

    @Test
    public void update() throws Exception {
        // 增数据
        PartnerSite partnerSite = new PartnerSite();
        partnerSite.setName(randomStr(4));
        partnerSite.setOpen(true);
        partnerSite.setDesc("");
        partnerSite.setIconPath("");
        partnerSite.setDelete(false);
        partnerSite.setUrl("https://" + randomStr(5) + ".celess.cn");
        mapper.insert(partnerSite);
        // 查数据
        PartnerSite latest = mapper.getLastest();
        assertNotNull(latest.getId());
        // 构建请求
        LinkReq linkReq = new LinkReq();
        linkReq.setUrl(latest.getUrl());
        linkReq.setOpen(!latest.getOpen());
        linkReq.setName(randomStr(4));
        linkReq.setId(latest.getId());

        getMockData(put("/admin/links/update"), adminLogin(), linkReq).andDo(result -> {
            Response<PartnerSite> response = getResponse(result, LINK_MODEL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PartnerSite site = response.getResult();
            assertNotNull(site.getId());
            assertEquals(linkReq.getId(), site.getId().longValue());
            assertEquals(linkReq.getUrl(), site.getUrl());
            assertEquals(linkReq.getName(), site.getName());
            assertEquals(linkReq.isOpen(), site.getOpen());
        });
    }

    @Test
    public void allForOpen() throws Exception {
        getMockData(get("/links")).andDo(result -> {
            Response<List<PartnerSite>> response = getResponse(result, LINK_MODEL_LIST_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            response.getResult().forEach(site -> {
                assertNotNull(site.getUrl());
                assertNull(site.getOpen());
                assertNotNull(site.getName());
            });
        });
    }

    @Test
    public void all() throws Exception {
        getMockData(get("/admin/links?page=1&count=10"), adminLogin()).andDo(result -> {
            Response<PageData<PartnerSite>> response = getResponse(result, LINK_MODEL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            PageData<PartnerSite> pageData = response.getResult();
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (PartnerSite site : pageData.getList()) {
                assertNotNull(site.getUrl());
                assertNotNull(site.getName());
                assertNotNull(site.getOpen());
            }
        });
    }

    // 手动测试
    // @Test
    public void apply() throws Exception {
        long l = System.currentTimeMillis();
        String url = "https://www.example.com";
        getMockData(post("/apply?name=小海博客Api测试，请忽略&url=" + url)).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result, OBJECT_TYPE).getCode()));
        System.out.println("耗时：" + (System.currentTimeMillis() - l) / 1000 + "s");
        url = "xxx";
        getMockData(post("/apply?name=小海博客Api测试，请忽略&url=" + url)).andDo(result -> assertEquals(PARAMETERS_URL_ERROR.getCode(), getResponse(result, OBJECT_TYPE).getCode()));

    }
}
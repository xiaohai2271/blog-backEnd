package cn.celess.controller;

import cn.celess.BaseTest;
import cn.celess.common.entity.Response;
import cn.celess.common.entity.WebUpdate;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.WebUpdateModel;
import cn.celess.common.mapper.WebUpdateInfoMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static cn.celess.common.enmu.ResponseEnum.DATA_NOT_EXIST;
import static cn.celess.common.enmu.ResponseEnum.SUCCESS;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Slf4j
public class WebUpdateInfoControllerTest extends BaseTest {

    private final TypeReference<?> MODAL_TYPE = new TypeReference<Response<WebUpdateModel>>() {
    };
    private final TypeReference<?> MODAL_LIST_TYPE = new TypeReference<Response<List<WebUpdateModel>>>() {
    };
    private final TypeReference<?> MODAL_PAGE_TYPE = new TypeReference<Response<PageData<WebUpdateModel>>>() {
    };


    @Autowired
    WebUpdateInfoMapper mapper;

    @Test
    public void create() throws Exception {
        String info = randomStr();
        getMockData(post("/admin/webUpdate/create?info=" + info), adminLogin()).andDo(result -> {
            Response<WebUpdateModel> response = getResponse(result, MODAL_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            WebUpdateModel webUpdateModel = response.getResult();
            assertEquals(info, webUpdateModel.getInfo());
            assertNotNull(webUpdateModel.getTime());
            assertNotEquals(0, webUpdateModel.getId());
        });
    }

    @Test
    public void del() throws Exception {
        // 新增数据
        WebUpdate webUpdate = new WebUpdate();
        webUpdate.setUpdateInfo(randomStr());
        webUpdate.setUpdateTime(new Date());
        mapper.insert(webUpdate);
        // 接口测试
        List<WebUpdate> updateList = mapper.findAll();
        WebUpdate update = updateList.get(updateList.size() - 1);
        assertNotEquals(0, update.getId());

        long id = update.getId();
        getMockData(delete("/admin/webUpdate/del/" + id), adminLogin()).andDo(result -> {
            Response<Object> response = getResponse(result);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
        });
        do {
            id += 2;
        } while (mapper.existsById(id));
        log.debug("准备删除ID={}的不存在记录", id);
        getMockData(delete("/admin/webUpdate/del/" + id), adminLogin()).andDo(result -> assertEquals(DATA_NOT_EXIST.getCode(), getResponse(result).getCode()));
    }

    @Test
    public void update() throws Exception {
        // 新增数据
        WebUpdate webUpdate = new WebUpdate();
        webUpdate.setUpdateInfo(randomStr());
        webUpdate.setUpdateTime(new Date());
        mapper.insert(webUpdate);
        List<WebUpdate> all = mapper.findAll();
        WebUpdate update = all.get(all.size() - 1);
        assertNotEquals(0, update.getId());
        assertNotNull(update.getUpdateInfo());
        String info = randomStr();
        getMockData(put("/admin/webUpdate/update?id=" + update.getId() + "&info=" + info), adminLogin()).andDo(result -> {
            List<WebUpdate> list = mapper.findAll();
            WebUpdate up = list.get(list.size() - 1);
            assertEquals(update.getId(), up.getId());
            assertEquals(update.getUpdateTime(), up.getUpdateTime());
            assertEquals(info, up.getUpdateInfo());
        });
    }

    @Test
    public void findAll() throws Exception {
        getMockData(get("/webUpdate")).andDo(result -> {
            Response<List<WebUpdateModel>> response = getResponse(result, MODAL_LIST_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            assertNotEquals(0, response.getResult());
            response.getResult().forEach(webUpdate -> {
                assertNotEquals(0, webUpdate.getId());
                assertNotNull(webUpdate.getTime());
                assertNotNull(webUpdate.getInfo());
            });
        });
    }

    @Test
    public void page() throws Exception {
        getMockData(get("/webUpdate/pages?page=1&count=10")).andDo(result -> {
            Response<PageData<WebUpdateModel>> response = getResponse(result, MODAL_PAGE_TYPE);
            assertEquals(SUCCESS.getCode(), response.getCode());
            assertNotNull(response.getResult());
            PageData<WebUpdateModel> pageData = response.getResult();
            assertEquals(1, pageData.getPageNum());
            assertEquals(10, pageData.getPageSize());
            for (WebUpdateModel model : pageData.getList()) {
                assertNotEquals(0, model.getId());
                assertNotNull(model.getTime());
                assertNotNull(model.getInfo());
            }
        });
    }

    @Test
    public void lastestUpdateTime() throws Exception {
        getMockData(get("/lastestUpdate")).andDo(result -> assertEquals(SUCCESS.getCode(), getResponse(result).getCode()));
    }
}
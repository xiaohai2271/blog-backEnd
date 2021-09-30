package cn.celess.common.mapper;

import cn.celess.common.CommonBaseTest;
import cn.celess.common.entity.WebUpdate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class WebUpdateInfoMapperTest extends CommonBaseTest {

    @Autowired
    WebUpdateInfoMapper webUpdateInfoMapper;

    @Test
    public void insert() {
        WebUpdate webUpdate = generateUpdateInfo();
        assertNotNull(webUpdate);
    }

    @Test
    public void delete() {
        WebUpdate webUpdate = generateUpdateInfo();
        assertEquals(1, webUpdateInfoMapper.delete(webUpdate.getId()));
        assertTrue(webUpdateInfoMapper.findById(webUpdate.getId()).isDelete());
    }

    @Test
    public void update() {
        WebUpdate webUpdate = generateUpdateInfo();
        assertEquals(1, webUpdateInfoMapper.update(webUpdate.getId(), randomStr(6)));
    }

    @Test
    public void existsById() {
        WebUpdate webUpdate = generateUpdateInfo();
        assertTrue(webUpdateInfoMapper.existsById(webUpdate.getId()));
    }

    @Test
    public void findById() {
        WebUpdate webUpdate = generateUpdateInfo();
        WebUpdate byId = webUpdateInfoMapper.findById(webUpdate.getId());
        assertEquals(webUpdate.getUpdateInfo(), byId.getUpdateInfo());
        assertEquals(webUpdate.getId(), byId.getId());
    }

    @Test
    public void findAll() {
        List<WebUpdate> all = webUpdateInfoMapper.findAll();
        assertNotEquals(0, all.size());
    }

    @Test
    public void findAllNotDeleted() {
        this.delete();
        List<WebUpdate> allNotDeleted = webUpdateInfoMapper.findAllNotDeleted();
        allNotDeleted.forEach(webUpdate -> assertFalse(webUpdate.isDelete()));
    }

    @Test
    public void getLastestOne() {
        WebUpdate webUpdate = generateUpdateInfo();
        List<WebUpdate> all = webUpdateInfoMapper.findAll();
        List<WebUpdate> allNotDeleted = webUpdateInfoMapper.findAllNotDeleted();
        all.sort(((o1, o2) -> (int) (o2.getId() - o1.getId())));
        allNotDeleted.sort(((o1, o2) -> (int) (o2.getId() - o1.getId())));
        assertEquals(webUpdate.getId(), all.get(0).getId());
        assertEquals(webUpdate.getId(), allNotDeleted.get(0).getId());
        assertEquals(webUpdate.getId(), webUpdateInfoMapper.getLastestOne().getId());
    }

    private WebUpdate generateUpdateInfo() {
        WebUpdate webUpdate = new WebUpdate(randomStr(8));
        webUpdateInfoMapper.insert(webUpdate);
        return webUpdate;
    }
}
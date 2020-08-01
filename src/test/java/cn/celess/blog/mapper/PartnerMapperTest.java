package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.PartnerSite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class PartnerMapperTest extends BaseTest {

    @Autowired
    PartnerMapper partnerMapper;

    @Test
    public void insert() {
        PartnerSite partnerSite = generatePartnerSite();
        assertNotNull(partnerSite.getId());
    }

    @Test
    public void delete() {
        PartnerSite partnerSite = generatePartnerSite();
        assertEquals(1, partnerMapper.delete(partnerSite.getId()));
        partnerSite = partnerMapper.findById(partnerSite.getId());
        assertTrue(partnerSite.getDelete());
    }

    @Test
    public void update() {
        PartnerSite partnerSite = generatePartnerSite();
        partnerSite.setName(randomStr(5));
        partnerSite.setIconPath(randomStr(5));
        partnerSite.setDesc(randomStr(5));
        partnerSite.setOpen(false);
        partnerSite.setUrl("www.celess.cn/?random=" + randomStr(4));
        assertEquals(1, partnerMapper.update(partnerSite));
    }

    @Test
    public void existsById() {
        PartnerSite partnerSite = generatePartnerSite();
        assertTrue(partnerMapper.existsById(partnerSite.getId()));
    }

    @Test
    public void existsByName() {
        PartnerSite partnerSite = generatePartnerSite();
        assertTrue(partnerMapper.existsByName(partnerSite.getName()));
    }

    @Test
    public void existsByUrl() {
        PartnerSite partnerSite = generatePartnerSite();
        assertTrue(partnerMapper.existsByUrl(partnerSite.getUrl()));
    }

    @Test
    public void findById() {
        PartnerSite partnerSite = generatePartnerSite();
        assertNotNull(partnerMapper.findById(partnerSite.getId()));
    }

    @Test
    public void findByName() {
        PartnerSite partnerSite = generatePartnerSite();
        assertNotNull(partnerMapper.findByName(partnerSite.getName()));
    }

    @Test
    public void findByUrl() {
        PartnerSite partnerSite = generatePartnerSite();
        assertNotNull(partnerMapper.findByUrl(partnerSite.getUrl()));
    }

    @Test
    public void getLastest() {
        PartnerSite partnerSite = generatePartnerSite();
        List<PartnerSite> all = partnerMapper.findAll();
        all.sort(((o1, o2) -> (int) (o2.getId() - o1.getId())));
        assertEquals(partnerSite.getId(), all.get(0).getId());
        assertEquals(partnerSite.getId(), partnerMapper.getLastest().getId());
    }

    @Test
    public void findAll() {
        List<PartnerSite> all = partnerMapper.findAll();
        assertNotEquals(0, all.size());
    }

    private PartnerSite generatePartnerSite() {
        PartnerSite ps = new PartnerSite();
        ps.setName(randomStr(4));
        ps.setUrl("https://www.celess.cn?random=" + randomStr(4));
        ps.setOpen(true);
        ps.setDesc("小海的博客呀!");
        ps.setIconPath("https://www.celess.cn/icon_path.example");
        partnerMapper.insert(ps);
        return ps;
    }
}
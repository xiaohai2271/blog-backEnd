package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Visitor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VisitorMapperTest extends BaseTest {

    @Autowired
    VisitorMapper visitorMapper;

    @Test
    public void insert() {
        Visitor visitor = new Visitor();
        visitor.setDate(new Date());
        visitor.setIp("127.0.0.1");
        visitor.setUa("ua");
        assertEquals(1, visitorMapper.insert(visitor));
    }

    @Test
    public void delete() {
        Visitor visitor = new Visitor();
        visitor.setDate(new Date());
        visitor.setIp("127.0.0.1");
        visitor.setUa("ua");
        visitorMapper.insert(visitor);
        assertEquals(1, visitorMapper.delete(visitor.getId()));
    }

    @Test
    public void count() {
        assertNotEquals(0, visitorMapper.count());
    }


}
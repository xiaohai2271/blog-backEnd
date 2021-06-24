package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Config;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ConfigMapperTest extends BaseTest {

    @Autowired
    ConfigMapper configMapper;

    @Test
    public void getConfiguration() {
        Config file = configMapper.getConfiguration("file.type");
        assertNotNull(file);
        assertNotNull(file.getId());
        assertEquals("file.type", file.getName());
        assertEquals("local", file.getValue());
    }

    @Test
    public void updateConfiguration() {
        Config config = generateConfig();
        configMapper.addConfiguration(config);
        assertNotNull(config.getId());

        String s = randomStr();
        config.setValue(s);
        configMapper.updateConfiguration(config);
        assertEquals(s, configMapper.getConfiguration(config.getName()).getValue());
    }

    @Test
    public void getConfigurations() {
        assertTrue(configMapper.getConfigurations().size() > 0);
    }

    @Test
    public void addConfiguration() {
        Config config = generateConfig();
        configMapper.addConfiguration(config);
        assertNotNull(config.getId());
    }

    @Test
    public void deleteConfiguration() {
        Config config = generateConfig();
        configMapper.addConfiguration(config);
        assertNotNull(config.getId());
        assertNotEquals(0, configMapper.deleteConfiguration(config.getId()));
        assertNull(configMapper.getConfiguration(config.getName()));
    }

    private Config generateConfig() {
        Config config = new Config();
        config.setName("test" + randomStr(4));
        config.setValue(randomStr(4));
        return config;
    }
}
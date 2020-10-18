package cn.celess.blog.configuration;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.service.fileserviceimpl.LocalFileServiceImpl;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DruidConfigTest extends BaseTest {

    @Mock
    Environment env;

    @Autowired
    Environment globalEnvironment;


    private File configFile;
    private File bakConfigFile;

    @Test
    public void initDataSource() throws IOException {

        DruidConfig druidConfig = new DruidConfig();

        druidConfig.env = env;

        System.out.println(Arrays.toString(env.getActiveProfiles()));

        // 无配置文件
        assertTrue(!configFile.exists() || configFile.delete());
        DruidDataSource druidDataSource = druidConfig.initDataSource();

        // 加载初始化时候配置文件的数据库连接
        assertEquals(env.getProperty(DruidConfig.DB_CONFIG_URL_PREFIX), druidDataSource.getUrl());
        assertEquals(env.getProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX), druidDataSource.getDriverClassName());
        assertEquals(env.getProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX), druidDataSource.getUsername());
        assertEquals(env.getProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX), druidDataSource.getPassword());
        assertTrue(configFile.createNewFile());

        // 有配置文件的测试
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(configFile);
        properties.load(fileInputStream);
        fileInputStream.close();
        properties.setProperty(DruidConfig.DB_CONFIG_URL_PREFIX, "jdbc:mysql://localhost:3306/blog");
        properties.setProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX, "com.mysql.cj.jdbc.Driver");
        properties.setProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX, "username");
        properties.setProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX, "password");
        // 保存到文件
        FileOutputStream fileOutputStream = new FileOutputStream(configFile);
        properties.store(fileOutputStream, "数据库配置");
        fileOutputStream.close();
        druidDataSource = druidConfig.initDataSource();
        assertEquals(properties.getProperty(DruidConfig.DB_CONFIG_URL_PREFIX), druidDataSource.getUrl());
        assertEquals(properties.getProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX), druidDataSource.getDriverClassName());
        assertEquals(properties.getProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX), druidDataSource.getUsername());
        assertEquals(properties.getProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX), druidDataSource.getPassword());
    }

    @After
    public void setConfigBack() {
        if (bakConfigFile.exists()) {
            System.out.println("恢复配置文件成功");
            copyFile(bakConfigFile, configFile);
            assertTrue(bakConfigFile.delete());
        } else {
            configFile.deleteOnExit();
        }
    }

    @Before
    public void recordConfig() {
        File path = new File(LocalFileServiceImpl.getPath(System.getProperty(ConfigKeyEnum.BLOG_FILE_PATH.getKey())));
        if (!path.exists() && !path.mkdirs()) {
            fail("创建失败");
        }
        this.bakConfigFile = new File(DruidConfig.DB_CONFIG_PATH + ".bak");
        this.configFile = new File(DruidConfig.DB_CONFIG_PATH);
        if (configFile.exists()) {
            System.out.println("备份文件成功");
            copyFile(configFile, bakConfigFile);
        }
        when(this.env.getActiveProfiles()).thenReturn(new String[]{"dev"});
        when(this.env.getProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX)).thenReturn(globalEnvironment.getProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX));
        when(this.env.getProperty(DruidConfig.DB_CONFIG_URL_PREFIX)).thenReturn(globalEnvironment.getProperty(DruidConfig.DB_CONFIG_URL_PREFIX));
        when(this.env.getProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX)).thenReturn(globalEnvironment.getProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX));
        when(this.env.getProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX)).thenReturn(globalEnvironment.getProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX));
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dist
     * @return
     */
    private boolean copyFile(File src, File dist) {
        try {
            // 复制备份文件
            FileOutputStream fos = new FileOutputStream(dist);
            FileInputStream fis = new FileInputStream(src);
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            fos.close();
            fis.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
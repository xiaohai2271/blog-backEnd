package cn.celess.blog.configuration;

import cn.celess.blog.BaseTest;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class DruidConfigTest extends BaseTest {

    @Autowired
    Environment env;

    private File configFile;
    private File bakConfigFile;

    @Test
    public void initDataSource() throws IOException {

        DruidConfig druidConfig = new DruidConfig();
        druidConfig.env = env;

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
        properties.load(new FileInputStream(configFile));
        properties.setProperty(DruidConfig.DB_CONFIG_URL_PREFIX, "jdbc:mysql://localhost:3306/blog");
        properties.setProperty(DruidConfig.DB_CONFIG_DRIVER_CLASS_NAME_PREFIX, "com.mysql.cj.jdbc.Driver");
        properties.setProperty(DruidConfig.DB_CONFIG_USERNAME_PREFIX, "username");
        properties.setProperty(DruidConfig.DB_CONFIG_PASSWORD_PREFIX, "password");
        // 保存到文件
        properties.store(new FileOutputStream(configFile), "db config");
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
            bakConfigFile.deleteOnExit();
        }
    }

    @Before
    public void recordConfig() throws IOException {
        this.bakConfigFile = new File(DruidConfig.DB_CONFIG_PATH + ".bak");
        this.configFile = new File(DruidConfig.DB_CONFIG_PATH);
        if (configFile.exists()) {
            System.out.println("备份文件成功");
            copyFile(configFile, bakConfigFile);
        }
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
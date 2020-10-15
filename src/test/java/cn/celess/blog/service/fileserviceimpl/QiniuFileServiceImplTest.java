package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.model.FileResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

@Slf4j
@ActiveProfiles("prod")
public class QiniuFileServiceImplTest extends BaseTest {

    @Autowired
    QiniuFileServiceImpl qiniuFileService;

    @SneakyThrows
    @Test
    public void uploadFile() {
        String fileName = "test." + randomStr(3);
        File file = new File(fileName);
        if (!file.exists() && file.createNewFile()) {
            // 创建文件
            log.debug("创建文件[{}]", fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            for (int i = 0; i < 100; i++) {
                outputStream.write(new byte[1024]);
            }
            outputStream.flush();
            outputStream.close();
        }
        FileResponse fileResponse = qiniuFileService.uploadFile(new FileInputStream(file), fileName);
        assertEquals(fileName, fileResponse.key);
    }

    @Test
    public void getFileList() {
    }

    @Test
    public void deleteFile() {
    }
}
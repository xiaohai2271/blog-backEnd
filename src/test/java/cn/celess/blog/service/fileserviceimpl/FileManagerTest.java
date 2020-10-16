package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.BaseTest;
import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import cn.celess.blog.service.FileManager;
import cn.celess.blog.service.FileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class FileManagerTest extends BaseTest {

    @Autowired
    FileService fileService;

    FileManager fileManager;

    @Test
    public void testUploadFile() {
        // 测试本地的文件上传
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "local");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof LocalFileServiceImpl);
        uploadFile();

        // 测试七牛云的文件上传
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "qiniu");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof QiniuFileServiceImpl);
        uploadFile();
    }

    @Test
    public void testGetFileList() {
        // 测试获取本地的文件列表
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "local");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof LocalFileServiceImpl);
        getFileList();

        // 测试获取七牛云的文件列表
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "qiniu");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof QiniuFileServiceImpl);
        getFileList();
    }

    @Test
    public void testDeleteFile() {
        // 测试删除本地文件
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "local");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof LocalFileServiceImpl);
        deleteFile();

        // 测试删除七牛云文件
        fileManager = null;
        System.setProperty(ConfigKeyEnum.FILE_TYPE.getKey(), "qiniu");
        fileManager = fileService.getFileManager();
        assertTrue(fileManager instanceof QiniuFileServiceImpl);
        deleteFile();
    }

    @SneakyThrows
    public void uploadFile() {
        String fileName = null;
        File file = createFile();
        FileResponse fileResponse = fileManager.uploadFile(new FileInputStream(file), file.getName());
        assertEquals(file.getName(), fileResponse.key);
        assertEquals(System.getProperty(ConfigKeyEnum.FILE_TYPE.getKey()), fileResponse.bucket);
        //        assertNotNull(fileResponse.hash);
        fileName = fileResponse.key;

        fileManager.deleteFile(fileName);

        file.deleteOnExit();
    }

    public void getFileList() {
        List<FileInfo> fileList = fileManager.getFileList();
        fileList.forEach(fileInfo -> {
            assertNotNull(fileInfo.key);
            //            assertNotNull(fileInfo.hash);
        });
    }

    public void deleteFile() {
        uploadFile();
    }

    @SneakyThrows
    private File createFile() {
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
        return file;
    }
}
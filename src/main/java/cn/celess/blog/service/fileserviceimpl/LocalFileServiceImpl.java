package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import cn.celess.blog.service.FileManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2020/10/16 14:39
 * @desc :
 */
@Slf4j
@Service("localFileServiceImpl")
public class LocalFileServiceImpl implements FileManager {
    private static String path = null;

    @SneakyThrows
    @Override
    public FileResponse uploadFile(InputStream is, String fileName) {
        if (path == null) {
            path = System.getProperty(ConfigKeyEnum.FILE_LOCAL_DIRECTORY_PATH.getKey());
        }
        // 判断文件夹是否存在
        File directory = new File(getPath(path));
        if (!directory.exists() && directory.mkdirs()) {
            log.info("不存在文件夹，创建文件夹=>{}", directory.getAbsolutePath());
        }
        log.info("上传文件 {}", fileName);
        // 存储文件
        File file1 = new File(getPath(path) + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file1);
        byte[] cache = new byte[1024 * 100];
        int len = 0;
        FileResponse fileResponse = new FileResponse();
        while ((len = is.read(cache)) != -1) {
            fileResponse.size += len;
            fos.write(cache, 0, len);
        }
        fos.flush();
        fos.close();
        is.close();
        fileResponse.key = URLEncoder.encode(fileName, "UTF-8");
        fileResponse.type = "local";
        return fileResponse;
    }

    @SneakyThrows
    @Override
    public List<FileInfo> getFileList() {
        if (path == null) {
            path = System.getProperty(ConfigKeyEnum.FILE_LOCAL_DIRECTORY_PATH.getKey());
        }
        File file = new File(getPath(path));
        File[] files = file.listFiles();
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (files == null) {
            return null;
        }
        for (File file1 : files) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.key = URLEncoder.encode(file1.getName(), "UTF-8");
            fileInfo.size = file1.length();
            BasicFileAttributes basicFileAttributes = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
            fileInfo.uploadTime = basicFileAttributes.creationTime().toMillis();
            fileInfoList.add(fileInfo);
        }
        return fileInfoList;
    }

    @Override
    public boolean deleteFile(String fileName) {
        if (path == null) {
            path = System.getProperty(ConfigKeyEnum.FILE_LOCAL_DIRECTORY_PATH.getKey());
        }
        File file = new File(getPath(path) + File.separator + fileName);
        return file.delete();
    }

    public static String getPath(String path) {
        if (path == null) {
            return "";
        }
        String pathCop = path.replaceAll("//", File.separator);
        if (path.startsWith("~")) {
            // 家目录
            pathCop = path.replaceFirst("~", "");
            pathCop = System.getProperty("user.home") + pathCop;
        }
        return pathCop;
    }
}

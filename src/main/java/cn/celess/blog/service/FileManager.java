package cn.celess.blog.service;

import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2020/10/15 18:19
 * @desc : 文件管理器 定义操作文件的方法
 */
@Service
public interface FileManager {
    /**
     * 上传文件到文件存储容器中
     *
     * @param is       文件流
     * @param fileName 文件名
     * @return FileResponse
     */
    FileResponse uploadFile(InputStream is, String fileName);

    /**
     * 获取文件列表
     *
     * @return 文件信息
     */
    List<FileInfo> getFileList();

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     */
    boolean deleteFile(String fileName);
}

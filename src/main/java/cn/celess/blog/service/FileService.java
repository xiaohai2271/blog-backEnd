package cn.celess.blog.service;

import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service("fileService")
@FunctionalInterface
public interface FileService {
    /**
     * 获取文件管理器
     *
     * @return 文件管理器 可以操作文件
     */
    FileManager getFileManager();
}

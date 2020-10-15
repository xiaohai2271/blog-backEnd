package cn.celess.blog.service.interfaces;

import cn.celess.blog.entity.model.QiniuResponse;
import com.qiniu.storage.model.FileInfo;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service
public interface FileService {
    /**
     * 获取文件管理器
     *
     * @return 文件管理器 可以操作文件
     */
    FileManager getFileManager();
}

package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import cn.celess.blog.service.FileManager;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2020/10/16 14:39
 * @desc :
 */
@Service("localFileServiceImpl")
public class LocalFileServiceImpl implements FileManager {
    @Override
    public FileResponse uploadFile(InputStream is, String fileName) {
        return null;
    }

    @Override
    public List<FileInfo> getFileList() {
        return null;
    }

    @Override
    public boolean deleteFile(String fileName) {
        return false;
    }
}

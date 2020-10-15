package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.service.FileManager;
import cn.celess.blog.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : xiaohai
 * @date : 2020/10/15 18:52
 * @desc :
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {
    @Resource(name = "qiniuFileServiceImpl")
    FileManager qiniuFileManager;

    @Override
    public FileManager getFileManager() {
        return qiniuFileManager;
    }
}

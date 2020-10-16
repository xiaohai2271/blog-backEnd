package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.enmu.ConfigKeyEnum;
import cn.celess.blog.service.FileManager;
import cn.celess.blog.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : xiaohai
 * @date : 2020/10/15 18:52
 * @desc : 提供文件管理器的服务
 */
@Slf4j
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {
    @Resource(name = "qiniuFileServiceImpl")
    FileManager qiniuFileManager;

    @Resource(name = "localFileServiceImpl")
    FileManager localFileServiceImpl;

    @Override
    public FileManager getFileManager() {
        String property = System.getProperty(ConfigKeyEnum.FILE_TYPE.getKey());
        log.info("获取到{}:{}", ConfigKeyEnum.FILE_TYPE.getKey(), property);
        if ("qiniu".equals(property)){
            return qiniuFileManager;
        }else if ("local".equals(property)){
            return localFileServiceImpl;
        }
        return localFileServiceImpl;
    }
}

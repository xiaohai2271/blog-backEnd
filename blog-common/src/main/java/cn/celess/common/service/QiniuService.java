package cn.celess.common.service;

import cn.celess.common.entity.vo.QiniuResponse;
import com.qiniu.storage.model.FileInfo;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service
public interface QiniuService {
    /**
     * 上传文件
     *
     * @param is       InputStream流
     * @param fileName 文件名
     * @return 响应数据
     */
    QiniuResponse uploadFile(InputStream is, String fileName);

    /**
     * 获取文件列表
     *
     * @return 文件列表
     */
    FileInfo[] getFileList();

}

package cn.celess.blog.service.fileserviceimpl;

import cn.celess.blog.entity.model.FileInfo;
import cn.celess.blog.entity.model.FileResponse;
import cn.celess.blog.service.FileManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service("qiniuFileServiceImpl")
public class QiniuFileServiceImpl implements FileManager {
    private static final Configuration cfg = new Configuration(Zone.zone2());
    private static UploadManager uploadManager;
    private static BucketManager bucketManager;
    private static Auth auth;

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;

    private void init() {
        if (auth == null || uploadManager == null || bucketManager == null) {
            auth = Auth.create(accessKey, secretKey);
            uploadManager = new UploadManager(cfg);
            bucketManager = new BucketManager(auth, cfg);
        }
    }

    @Override
    public FileResponse uploadFile(InputStream is, String fileName) {
        init();
        //文件存在则删除文件
        if (continueFile(fileName)) {
            try {
                System.out.println(bucketManager.delete(bucket, fileName).toString());
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        //上传
        try {
            Response response = uploadManager.put(is, fileName, auth.uploadToken(bucket), null, null);
            FileResponse fileResponse = new FileResponse();
            StringMap stringMap = response.jsonToMap();

            fileResponse.key = (String) stringMap.get("key");
            fileResponse.bucket = "qiniu";
            fileResponse.size = 0;
            fileResponse.hash = (String) stringMap.get("hash");

            return fileResponse;
        } catch (QiniuException e) {
            Response r = e.response;
            System.err.println(r.toString());
            return null;
        }
    }

    @Override
    public List<FileInfo> getFileList() {
        init();
        List<FileInfo> infoList = null;
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, "", 1000, "");
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            infoList = new ArrayList<com.qiniu.storage.model.FileInfo>(Arrays.asList(fileListIterator.next()))
                    .stream()
                    .map(item -> {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.key = item.key;
                        fileInfo.hash = item.hash;
                        fileInfo.size = item.fsize;
                        fileInfo.uploadTime = item.putTime;
                        return fileInfo;
                    })
                    .collect(Collectors.toList());
        }
        return infoList;
    }

    @SneakyThrows
    @Override
    public boolean deleteFile(String fileName) {
        init();
        Response response = bucketManager.delete(bucket, fileName);
        return "".equals(response.bodyString());
    }

    private boolean continueFile(String key) {
        List<FileInfo> fileList = getFileList();
        for (FileInfo fileInfo : fileList) {
            if (key.equals(fileInfo.key)) {
                return true;
            }
        }
        return false;
    }
}

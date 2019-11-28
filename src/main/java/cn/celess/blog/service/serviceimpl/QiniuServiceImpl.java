package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.entity.model.QiniuResponse;
import cn.celess.blog.service.QiniuService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author : xiaohai
 * @date : 2019/04/25 18:15
 */
@Service
public class QiniuServiceImpl implements QiniuService {
    private Configuration cfg = new Configuration(Zone.zone2());
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private Auth auth;

    private static String bucket;


    {
        /* ***** 必填 ******
         *    七牛的配置    *
         * ***** 必填 ******
         */
        // accessKeyString,secretKeyString,bucketString：请替换为自己的值
        String accessKey = "accessKeyString";
        String secretKey = "secretKeyString";
        bucket = "bucketString";

        auth = Auth.create(accessKey, secretKey);
        uploadManager = new UploadManager(cfg);
        bucketManager = new BucketManager(auth, cfg);
    }

    @Override
    public QiniuResponse uploadFile(InputStream is, String fileName) {
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
            return response.jsonToObject(QiniuResponse.class);
        } catch (QiniuException e) {
            Response r = e.response;
            System.err.println(r.toString());
            return null;
        }
    }

    @Override
    public FileInfo[] getFileList() {
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, "", 1000, "");
        FileInfo[] items = null;
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            items = fileListIterator.next();
        }
        return items;
    }

    private boolean continueFile(String key) {
        FileInfo[] allFile = getFileList();
        for (FileInfo fileInfo : allFile) {
            if (key.equals(fileInfo.key)) {
                return true;
            }
        }
        return false;
    }
}

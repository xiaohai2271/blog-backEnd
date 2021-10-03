package cn.celess.visitor.util;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author : xiaohai
 * @date : 2020/09/04 9:36
 */

@Slf4j
public class AddressUtil {

    public static String getCityInfo(String ip) {
        File file;
        try {
            //db
            ClassPathResource resource = new ClassPathResource("ip2region/ip2region.db");
            File tempDir = Files.createTempDir();
            tempDir.deleteOnExit();
            file = new File(tempDir, "ip.db");
            FileCopyUtils.copy(Objects.requireNonNull(resource.getInputStream()), new FileOutputStream(file));

            //查询算法
            //B-tree
            int algorithm = DbSearcher.BTREE_ALGORITHM;
            try {
                DbConfig config = new DbConfig();
                DbSearcher searcher = new DbSearcher(config, file.getAbsolutePath());
                Method method = null;
                switch (algorithm) {
                    case DbSearcher.BTREE_ALGORITHM:
                        method = searcher.getClass().getMethod("btreeSearch", String.class);
                        break;
                    case DbSearcher.BINARY_ALGORITHM:
                        method = searcher.getClass().getMethod("binarySearch", String.class);
                        break;
                    case DbSearcher.MEMORY_ALGORITYM:
                        method = searcher.getClass().getMethod("memorySearch", String.class);
                        break;
                }

                DataBlock dataBlock;
                if (!Util.isIpAddress(ip)) {
                    System.out.println("Error: Invalid ip address");
                }

                dataBlock = (DataBlock) method.invoke(searcher, ip);

                return dataBlock.getRegion();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

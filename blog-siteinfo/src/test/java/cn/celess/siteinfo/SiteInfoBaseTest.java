package cn.celess.siteinfo;

import cn.celess.common.test.BaseTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SiteInfoApplication.class)
@RunWith(SpringRunner.class)
public abstract class SiteInfoBaseTest extends BaseTest {
}

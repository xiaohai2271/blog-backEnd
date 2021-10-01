package cn.celess.partnersite;

import cn.celess.common.test.BaseRedisTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = PartnerSiteApplication.class)
@RunWith(SpringRunner.class)
public abstract class PartnerSiteBaseTest extends BaseRedisTest {
}

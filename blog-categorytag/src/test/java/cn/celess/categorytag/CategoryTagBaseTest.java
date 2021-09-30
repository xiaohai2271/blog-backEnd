package cn.celess.categorytag;

import cn.celess.common.test.BaseTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {CategoryTagApplication.class})
@RunWith(SpringRunner.class)
public abstract class CategoryTagBaseTest extends BaseTest {
}

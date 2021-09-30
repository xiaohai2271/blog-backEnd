package cn.celess;

import cn.celess.common.test.BaseTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {BlogApplication.class})
@RunWith(SpringRunner.class)
public abstract class DeployBaseTest extends BaseTest {
}

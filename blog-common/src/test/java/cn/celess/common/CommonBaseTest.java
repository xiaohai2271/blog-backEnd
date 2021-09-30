package cn.celess.common;

import cn.celess.common.test.BaseTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CommonApplication.class)
@RunWith(SpringRunner.class)
public abstract class CommonBaseTest extends BaseTest {
}

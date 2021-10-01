package cn.celess.visitor;

import cn.celess.common.test.BaseRedisTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = VisitorApplication.class)
@RunWith(SpringRunner.class)
public abstract class VisitorBaseTest extends BaseRedisTest {
}

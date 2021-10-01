package cn.celess.user;

import cn.celess.common.test.BaseRedisTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Component
public abstract class UserBaseTest extends BaseRedisTest {
}

package cn.celess.article;

import cn.celess.common.test.RedisServerMock;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {ArticleApplication.class})
@RunWith(SpringRunner.class)
public abstract class ArticleBaseTest extends RedisServerMock {
}

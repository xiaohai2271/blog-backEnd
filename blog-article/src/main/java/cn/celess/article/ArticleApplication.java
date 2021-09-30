package cn.celess.article;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {ArticleApplication.class, CommonApplication.class, UserApplication.class})
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}

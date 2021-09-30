package cn.celess.categorytag;


import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {CategoryTagApplication.class, CommonApplication.class, UserApplication.class})
public class CategoryTagApplication {
    public static void main(String[] args) {
        SpringApplication.run(CategoryTagApplication.class, args);
    }
}

package cn.celess.categorytag;


import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {CategoryTagApplication.class, CommonApplication.class, UserApplication.class})
public class CategoryTagApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CategoryTagApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

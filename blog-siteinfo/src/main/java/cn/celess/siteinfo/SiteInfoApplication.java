package cn.celess.siteinfo;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {SiteInfoApplication.class, CommonApplication.class, UserApplication.class})
public class SiteInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiteInfoApplication.class, args);
    }
}

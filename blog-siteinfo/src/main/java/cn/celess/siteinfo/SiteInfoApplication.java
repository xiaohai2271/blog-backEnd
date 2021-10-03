package cn.celess.siteinfo;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {SiteInfoApplication.class, CommonApplication.class, UserApplication.class})
public class SiteInfoApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SiteInfoApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

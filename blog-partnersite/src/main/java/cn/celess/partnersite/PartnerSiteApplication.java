package cn.celess.partnersite;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {PartnerSiteApplication.class, CommonApplication.class, UserApplication.class})
public class PartnerSiteApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PartnerSiteApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

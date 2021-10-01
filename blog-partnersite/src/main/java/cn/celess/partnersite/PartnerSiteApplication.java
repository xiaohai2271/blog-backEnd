package cn.celess.partnersite;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {PartnerSiteApplication.class, CommonApplication.class, UserApplication.class})
public class PartnerSiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(PartnerSiteApplication.class, args);
    }
}

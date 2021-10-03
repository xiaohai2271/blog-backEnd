package cn.celess.visitor;

import cn.celess.common.CommonApplication;
import cn.celess.extension.ExtensionApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {
        VisitorApplication.class,
        CommonApplication.class,
        ExtensionApplication.class,
        UserApplication.class
})
public class VisitorApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(VisitorApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

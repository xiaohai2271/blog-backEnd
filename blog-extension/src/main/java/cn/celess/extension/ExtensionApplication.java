package cn.celess.extension;

import cn.celess.common.CommonApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {ExtensionApplication.class, CommonApplication.class})
public class ExtensionApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ExtensionApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

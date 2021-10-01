package cn.celess.extension;

import cn.celess.common.CommonApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {ExtensionApplication.class, CommonApplication.class})
public class ExtensionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtensionApplication.class, args);
    }
}

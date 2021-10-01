package cn.celess.visitor;

import cn.celess.common.CommonApplication;
import cn.celess.extension.ExtensionApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {
        VisitorApplication.class,
        CommonApplication.class,
        ExtensionApplication.class,
        UserApplication.class
})
public class VisitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(VisitorApplication.class, args);
    }
}

package cn.celess.comment;

import cn.celess.common.CommonApplication;
import cn.celess.user.UserApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.SpringVersion;

@SpringBootApplication(scanBasePackageClasses = {CommentApplication.class, CommonApplication.class, UserApplication.class})
public class CommentApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CommentApplication.class)
                .main(SpringVersion.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}

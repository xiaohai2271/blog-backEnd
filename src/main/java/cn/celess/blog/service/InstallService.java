package cn.celess.blog.service;

import cn.celess.blog.entity.InstallParam;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author : xiaohai
 * @date : 2020/10/23 16:22
 * @desc :
 */
@Service
public interface InstallService {
    Map<String, Object> isInstall();

    Map<String, Object> install(@NotNull InstallParam installParam);

}

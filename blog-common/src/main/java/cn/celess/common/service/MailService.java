package cn.celess.common.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/22 14:25
 */
@Service
public interface MailService {
    /**
     * 异步发生邮件
     *
     * @param message SimpleMailMessage对象
     * @return //
     */
    Boolean AsyncSend(SimpleMailMessage message);

    /**
     * 同步发送邮件
     *
     * @param message SimpleMailMessage对象
     * @return 发送状态
     */
    Boolean send(SimpleMailMessage message);
}

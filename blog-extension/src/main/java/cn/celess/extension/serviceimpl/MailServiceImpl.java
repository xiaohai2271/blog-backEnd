package cn.celess.extension.serviceimpl;

import cn.celess.common.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author : xiaohai
 * @date : 2019/04/22 14:26
 */
@Service
public class MailServiceImpl implements MailService {
    public static final String FROM = "<xiaohai2271@163.com>";
    @Resource
    JavaMailSender javaMailSender;

    @Override
    @Async
    public Boolean AsyncSend(SimpleMailMessage message) {
        this.send(message);
        return true;
    }

    @Override
    public Boolean send(SimpleMailMessage message) {
        String nick = null;
        try {
            nick = javax.mail.internet.MimeUtility.encodeText("小海博客");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.setFrom(nick + FROM);
        javaMailSender.send(message);
        return true;
    }
}

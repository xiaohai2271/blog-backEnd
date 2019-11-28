package cn.celess.blog.controller;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.service.MailService;
import cn.celess.blog.service.PartnerSiteService;
import cn.celess.blog.util.RedisUtil;
import cn.celess.blog.util.RegexUtil;
import cn.celess.blog.util.ResponseUtil;
import cn.celess.blog.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/05/12 13:26
 */
@RestController
public class LinksController {
    @Autowired
    PartnerSiteService partnerSiteService;
    @Autowired
    MailService mailService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    HttpServletRequest request;

    @PostMapping("/admin/links/create")
    public Response create(@RequestBody LinkReq reqBody) {
        return ResponseUtil.success(partnerSiteService.create(reqBody));
    }

    @DeleteMapping("/admin/links/del/{id}")
    public Response del(@PathVariable("id") long id) {
        return ResponseUtil.success(partnerSiteService.del(id));
    }

    @PutMapping("/admin/links/update")
    public Response update(@RequestBody LinkReq reqBody) {
        return ResponseUtil.success(partnerSiteService.update(reqBody));
    }

    @GetMapping("/links")
    public Response allForOpen() {
        List<PartnerSite> sites = new ArrayList<>();
        for (PartnerSite p : partnerSiteService.findAll()) {
            if (p.getOpen()) {
                //隐藏open字段
                p.setOpen(null);
                sites.add(p);
            }
        }
        return ResponseUtil.success(sites);
    }

    @GetMapping("/admin/links")
    public Response all(@RequestParam("page") int page,
                        @RequestParam("count") int count) {
        return ResponseUtil.success(partnerSiteService.PartnerSitePages(page, count));
    }

    @PostMapping("/apply")
    public Response apply(@RequestParam("name") String name,
                          @RequestParam("url") String url) {
        // TODO :: 弃用发送邮件的方式。
        if (name == null || name.replaceAll(" ", "").isEmpty()) {
            return ResponseUtil.response(ResponseEnum.PARAMETERS_ERROR, null);
        }
        if (!RegexUtil.urlMatch(url)) {
            return ResponseUtil.response(ResponseEnum.PARAMETERS_URL_ERROR, null);
        }
        String applyTimeStr = redisUtil.get(request.getRemoteAddr() + "-Apply");
        int applyTime = 0;
        if (applyTimeStr != null) {
            applyTime = Integer.parseInt(applyTimeStr);
        }
        if (applyTime == 10) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "申请次数已达10次，请2小时后重试");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("友链申请：" + name);
        message.setTo("a@celess.cn");
        message.setText("name:" + name + "\nurl:" + url + "\n" + DateFormatUtil.getNow());
        Boolean send = mailService.send(message);
        redisUtil.setEx(request.getRemoteAddr() + "-Apply", applyTime + 1 + "", 2, TimeUnit.HOURS);
        return send ? ResponseUtil.success("") : ResponseUtil.failure("");

    }
}

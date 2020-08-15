package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkApplyReq;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.PartnerMapper;
import cn.celess.blog.service.MailService;
import cn.celess.blog.service.PartnerSiteService;
import cn.celess.blog.util.HttpUtil;
import cn.celess.blog.util.JwtUtil;
import cn.celess.blog.util.RedisUtil;
import cn.celess.blog.util.RegexUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:43
 */
@Service
public class PartnerSiteServiceImpl implements PartnerSiteService {
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    MailService mailService;
    @Autowired
    RedisUtil redisUtil;
    private static final String SITE_NAME = "小海博客";
    private static final String SITE_URL = "celess.cn";
    private static final String SITE_EMAIL = "a@celess.cn";

    @Override
    public PartnerSite create(LinkReq reqBody) {
        if (reqBody == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //判空
        if (reqBody.getName() == null || reqBody.getUrl() == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //判空
        if (reqBody.getName().replaceAll(" ", "").isEmpty() || reqBody.getUrl().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //是否存在 同名
        if (partnerMapper.existsByName(reqBody.getName())) {
            throw new MyException(ResponseEnum.DATA_HAS_EXIST);
        }
        //url是否合法
        if (!RegexUtil.urlMatch(reqBody.getUrl())) {
            throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
        }
        PartnerSite partnerSite = new PartnerSite();
        reqBody.setId(0);
        if (!reqBody.getUrl().contains("http://") && !reqBody.getUrl().contains("https://")) {
            reqBody.setUrl("http://" + reqBody.getUrl());
        }
        BeanUtils.copyProperties(reqBody, partnerSite);
        if (reqBody.getIconPath() == null) {
            partnerSite.setIconPath("");
        }
        if (reqBody.getDesc() == null) {
            partnerSite.setDesc("");
        }
        partnerMapper.insert(partnerSite);
        return partnerSite;
    }

    @Override
    public Boolean del(long id) {
        //判断数据是否存在
        if (!partnerMapper.existsById(id)) {
            throw new MyException(ResponseEnum.DATA_NOT_EXIST);
        }
        return partnerMapper.delete(id) == 1;
    }

    @Override
    public PartnerSite update(LinkReq reqBody) {
        PartnerSite partnerSite = partnerMapper.findById(reqBody.getId());
        if (partnerSite == null) {
            throw new MyException(ResponseEnum.DATA_NOT_EXIST);
        }
        if (partnerMapper.existsByName(reqBody.getName()) && !reqBody.getName().equals(partnerSite.getName())) {
            throw new MyException(ResponseEnum.DATA_HAS_EXIST);
        }
        if (!RegexUtil.urlMatch(reqBody.getUrl())) {
            throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
        }
        if (!reqBody.getUrl().contains("http://") && !reqBody.getUrl().contains("https://")) {
            reqBody.setUrl("http://" + reqBody.getUrl());
        }
        if (reqBody.isOpen() != partnerSite.getOpen() && !partnerSite.getNotification() && !StringUtils.isEmpty(partnerSite.getEmail())) {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setTo(partnerSite.getEmail());
            smm.setText("您的友链申请，已通过");
            smm.setSubject("友链申请通过");
            smm.setSentDate(new Date());
            mailService.send(smm);
            partnerSite.setNotification(true);
        }
        BeanUtils.copyProperties(reqBody, partnerSite);
        partnerMapper.update(partnerSite);
        partnerSite.setName(reqBody.getName());
        partnerSite.setUrl(reqBody.getUrl());
        partnerSite.setOpen(reqBody.isOpen());

        return partnerSite;
    }

    @Override
    public PageData<PartnerSite> partnerSitePages(int page, int count) {
        PageHelper.startPage(page, count);
        List<PartnerSite> sitePage = partnerMapper.findAll();
        PageInfo<PartnerSite> pageInfo = new PageInfo<PartnerSite>(sitePage);
        return new PageData<>(pageInfo, sitePage);
    }

    @Override
    public List<PartnerSite> findAll() {
        List<PartnerSite> all = partnerMapper.findAll();
        all.forEach(partnerSite -> partnerSite.setDelete(null));
        return all;
    }

    @SneakyThrows
    @Override
    public PartnerSite apply(LinkApplyReq linkApplyReq) {
        // 空值字段
        if (StringUtils.isEmpty(linkApplyReq.getName())
                || StringUtils.isEmpty(linkApplyReq.getUrl())
                || StringUtils.isEmpty(linkApplyReq.getEmail())
                || StringUtils.isEmpty(linkApplyReq.getLinkUrl())) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        // 链接不合法
        if (!RegexUtil.emailMatch(linkApplyReq.getEmail())) {
            throw new MyException(ResponseEnum.PARAMETERS_EMAIL_ERROR);
        }
        if (!RegexUtil.urlMatch(linkApplyReq.getLinkUrl()) || !RegexUtil.urlMatch(linkApplyReq.getUrl())) {
            throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
        }
        if (!StringUtils.isEmpty(linkApplyReq.getIconPath()) && !RegexUtil.urlMatch(linkApplyReq.getIconPath())) {
            throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
        }
        // 非强制字段 设置空
        if (StringUtils.isEmpty(linkApplyReq.getIconPath())) {
            linkApplyReq.setIconPath("");
        }
        // 抓取页面
        String resp = HttpUtil.getAfterRendering(linkApplyReq.getLinkUrl());
        if (resp == null) {
            throw new MyException(ResponseEnum.CANNOT_GET_DATA);
        }
        PartnerSite ps = new PartnerSite();
        if (resp.contains(SITE_URL)) {
            //包含站点
            BeanUtils.copyProperties(linkApplyReq, ps);
            ps.setNotification(false);
            ps.setOpen(false);
            boolean exists = partnerMapper.existsByUrl(linkApplyReq.getUrl());
            if (!exists) {
                partnerMapper.insert(ps);
            } else {
                ps.setId(partnerMapper.findByUrl(linkApplyReq.getUrl()).getId());
            }
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setSubject("友链申请");
            smm.setText("有一条友链申请" + (exists ? "，已存在的友链链接" : "") + "，[\n" + linkApplyReq.toString() + "\n]");
            smm.setTo(SITE_EMAIL);
            smm.setSentDate(new Date());
            mailService.send(smm);
        } else {
            //  不包含站点
            String uuid;
            ObjectMapper mapper = new ObjectMapper();
            if (redisUtil.hasKey(linkApplyReq.getUrl())) {
                uuid = redisUtil.get(linkApplyReq.getUrl());
                if (!redisUtil.hasKey(uuid)) {
                    redisUtil.setEx(uuid, mapper.writeValueAsString(linkApplyReq), 10, TimeUnit.MINUTES);
                }
            } else {
                uuid = UUID.randomUUID().toString().replaceAll("-", "");
                redisUtil.setEx(uuid, mapper.writeValueAsString(linkApplyReq), 10, TimeUnit.MINUTES);
                redisUtil.setEx(linkApplyReq.getUrl(), uuid, 10, TimeUnit.MINUTES);
            }
            throw new MyException(ResponseEnum.APPLY_LINK_NO_ADD_THIS_SITE, null, uuid);
        }
        return ps;
    }

    @SneakyThrows
    @Override
    public String reapply(String key) {
        if (!redisUtil.hasKey(key)) {
            throw new MyException(ResponseEnum.DATA_EXPIRED);
        }
        String s = redisUtil.get(key);
        ObjectMapper mapper = new ObjectMapper();
        LinkApplyReq linkApplyReq = mapper.readValue(s, LinkApplyReq.class);
        if (linkApplyReq == null) {
            throw new MyException(ResponseEnum.DATA_NOT_EXIST);
        }
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setSubject("友链申请");
        smm.setText("有一条未抓取到信息的友链申请，[\n" + linkApplyReq.toString() + "\n]");
        smm.setTo(SITE_EMAIL);
        smm.setSentDate(new Date());
        mailService.send(smm);
        redisUtil.delete(key);
        redisUtil.delete(linkApplyReq.getUrl());
        return "success";
    }
}

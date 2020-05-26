package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.PartnerMapper;
import cn.celess.blog.service.PartnerSiteService;
import cn.celess.blog.util.RegexUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:43
 */
@Service
public class PartnerSiteServiceImpl implements PartnerSiteService {
    @Autowired
    PartnerMapper partnerMapper;

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
        partnerSite.setName(reqBody.getName());
        partnerSite.setUrl(reqBody.getUrl());
        partnerSite.setOpen(reqBody.isOpen());
        partnerMapper.update(partnerSite);
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


}

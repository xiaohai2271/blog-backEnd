package cn.celess.blog.service;

import cn.celess.blog.entity.PartnerSite;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.LinkReq;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/05/12 11:42
 */
@Service
public interface PartnerSiteService {
    /**
     * 新增数据
     *
     * @param reqBody 数据请求体
     * @return 新增数据
     */
    PartnerSite create(LinkReq reqBody);

    /**
     * 删除数据
     *
     * @param id 数据id
     * @return 删除状态
     */
    Boolean del(long id);

    /**
     * 更新数据
     *
     * @param reqBody 数据请求体
     * @return 更新后的数据
     */
    PartnerSite update(LinkReq reqBody);

    /**
     * 分页获取数据
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageData<PartnerSite> partnerSitePages(int page, int count);

    /**
     * 获取全部数据
     *
     * @return 全部友链数据
     */
    List<PartnerSite> findAll();

}

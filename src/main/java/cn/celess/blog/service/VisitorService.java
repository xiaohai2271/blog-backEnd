package cn.celess.blog.service;

import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.VisitorModel;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/04/02 23:03
 */
@Service
public interface VisitorService {
    /**
     * 分页获取访客数据
     *
     * @param count        单页数据量
     * @param page         数据页
     * @param showLocation 是否显示位置信息 开启改选项数据响应超慢
     * @return 分页数据
     */
    PageData<VisitorModel> visitorPage(int page, int count, boolean showLocation);

    /**
     * 新增访客
     *
     * @param request HttpServletRequest
     * @return 返回状态  null: 访客信息已记录、爬虫
     */
    VisitorModel addVisitor(HttpServletRequest request);

    /**
     * 获取位置信息
     *
     * @param ip ip地址
     * @return 位置信息
     */
    String location(String ip);
}

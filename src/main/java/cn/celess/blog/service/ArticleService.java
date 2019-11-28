package cn.celess.blog.service;

import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;


/**
 * @author : xiaohai
 * @date : 2019/03/28 15:20
 */
@Service
public interface ArticleService {
    /**
     * 新增一篇文章
     *
     * @param reqBody 请求文章的数据
     * @return 文章数据
     */
    ArticleModel create(ArticleReq reqBody);

    /**
     * 删除一篇文章
     *
     * @param articleID 文章id
     * @return 删除状态  true：删除成功   false：失败
     */
    boolean delete(long articleID);

    /**
     * 更新一篇文章
     *
     * @param reqBody 请求数据
     * @return 文章数据
     */
    ArticleModel update(ArticleReq reqBody);

    /**
     * 获取一篇文章的数据
     *
     * @param articleID 文章id
     * @param is4update 是否是因文章更新而请求数据
     * @return 文章数据
     */
    ArticleModel retrieveOneByID(long articleID, boolean is4update);

    /**
     * 管理员 获取分页数据
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo adminArticles(int count, int page);

    /**
     * 获取文章状态为开放的文章
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo retrievePageForOpen(int count, int page);

    /**
     * 根据分类名获取文章数据
     *
     * @param name  分类名
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo findByCategory(String name, int page, int count);

    /**
     * 根据标签名获取文章数据
     *
     * @param name  标签名
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo findByTag(String name, int page, int count);
}

package cn.celess.blog.service;

import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/02 22:04
 */
@Service
public interface CountService {
    /**
     * 获取评论的数据量
     *
     * @return 评论的数据量
     */
    long getCommentCount();

    /**
     * 获取文章的篇数
     *
     * @return 文章的篇数
     */
    long getArticleCount();

    /**
     * 获取分类数量
     *
     * @return 分类数量
     */
    long getCategoriesCount();

    /**
     * 获取标签数量
     *
     * @return 标签数量
     */
    long getTagsCount();

    /**
     * 获取留言数量
     *
     * @return 留言数量
     */
    long getLeaveMessageCount();

    /**
     * 获取用户量
     *
     * @return 用户量
     */
    long getUserCount();

    /**
     * 获取总访问量
     *
     * @return 总访问量
     */
    long getVisitorCount();

    /**
     * 获取日访问量
     *
     * @return 日访问量
     */
    long getDayVisitCount();
}

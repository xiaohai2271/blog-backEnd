package cn.celess.blog.service;

import cn.celess.blog.entity.model.CommentModel;
import cn.celess.blog.entity.request.CommentReq;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/29 16:58
 */
@Service
public interface CommentService {
    /**
     * 新增数据
     *
     * @param reqBody 请求数据体
     * @return 增加的comment数据
     */
    CommentModel create(CommentReq reqBody);

    /**
     * 删除数据
     *
     * @param id comment的id
     * @return 删除状态
     */
    boolean delete(long id);

    /**
     * 更新数据
     *
     * @param reqBody comment请求体
     * @return 更新后的数据
     */
    CommentModel update(CommentReq reqBody);

    /**
     * 分页获取数据
     *
     * @param isComment true：评论   false：留言
     * @param count     单页数据量
     * @param page      数据页
     * @return 分页数据
     */
    PageInfo<CommentModel> retrievePage(Boolean isComment, int page, int count);

    /**
     * 通过pid获取数据
     *
     * @param pid   父id
     * @return 分页数据
     */
    List<CommentModel> retrievePageByPid(long pid);


    /**
     * 根据评论者获取数据
     *
     * @param isComment true：评论   false：留言
     * @param count     单页数据量
     * @param page      数据页
     * @return 分页数据
     */
    PageInfo<CommentModel> retrievePageByAuthor(Boolean isComment, int page, int count);

    /**
     * 根据文章获取数据
     *
     * @param articleID 文章id
     * @param pid       父id
     * @param count     单页数据量
     * @param page      数据页
     * @return 分页数据
     */
    PageInfo<CommentModel> retrievePageByArticle(long articleID, long pid, int page, int count);

    /**
     * 根据数据的type和pid获取数据
     *
     * @param isComment true：评论   false：留言
     * @param pid       父id
     * @param count     单页数据量
     * @param page      数据页
     * @return 分页数据
     */
    PageInfo<CommentModel> retrievePageByTypeAndPid(Boolean isComment, int pid, int page, int count);

    /**
     * 根据type获取数据
     *
     * @param isComment true：评论   false：留言
     * @param count     单页数据量
     * @param page      数据页
     * @return 分页数据
     */
    PageInfo<CommentModel> retrievePageByType(Boolean isComment, int page, int count);

}

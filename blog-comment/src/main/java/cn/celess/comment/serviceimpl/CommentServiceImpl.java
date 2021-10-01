package cn.celess.comment.serviceimpl;

import cn.celess.common.enmu.CommentStatusEnum;
import cn.celess.common.enmu.ResponseEnum;
import cn.celess.common.entity.Comment;
import cn.celess.common.entity.User;
import cn.celess.common.entity.dto.CommentReq;
import cn.celess.common.entity.vo.CommentModel;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.exception.BlogResponseException;
import cn.celess.common.mapper.ArticleMapper;
import cn.celess.common.mapper.CommentMapper;
import cn.celess.common.mapper.UserMapper;
import cn.celess.common.service.CommentService;
import cn.celess.common.util.ModalTrans;
import cn.celess.user.util.RedisUserUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : xiaohai
 * @date : 2019/03/29 17:05
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisUserUtil redisUserUtil;

    @Override
    public CommentModel create(CommentReq reqBody) {
        if (reqBody == null) {
            throw new BlogResponseException(ResponseEnum.PARAMETERS_ERROR);
        }
        User user = redisUserUtil.get();
        Comment pComment = null;
        if (reqBody.getPid() != -1) {
            pComment = commentMapper.findCommentById(reqBody.getPid());
        }
        //不是一级评论
        if (reqBody.getPid() != -1 && pComment == null) {
            //父评论不存在
            throw new BlogResponseException(ResponseEnum.PARAMETERS_ERROR);
        }
        Comment comment = new Comment();
        comment.setFromUser(user);
        User userTo = new User();
        userTo.setId(null);
        if (reqBody.getToUserId() != -1) {
            userTo = userMapper.findById(reqBody.getToUserId());
            comment.setToUser(userTo);
        }
        comment.setToUser(userTo);
        userMapper.findById(reqBody.getToUserId());
        BeanUtils.copyProperties(reqBody, comment);
        commentMapper.insert(comment);
        return ModalTrans.comment(commentMapper.findCommentById(comment.getId()));
    }

    @Override
    public boolean delete(long id) {
        Comment b = commentMapper.findCommentById(id);
        if (b == null) {
            throw new BlogResponseException(ResponseEnum.COMMENT_NOT_EXIST);
        }
        if (b.getStatus() == CommentStatusEnum.DELETED.getCode()) {
            throw new BlogResponseException(ResponseEnum.DATA_IS_DELETED);
        }
        commentMapper.delete(id);
        return true;
    }

    @Override
    public CommentModel update(CommentReq reqBody) {
        if (reqBody.getId() == null) {
            throw new BlogResponseException(ResponseEnum.PARAMETERS_ERROR.getCode(), "id不可为空");
        }
        Comment comment = commentMapper.findCommentById(reqBody.getId());
        if (!comment.getContent().equals(reqBody.getContent())) {
            commentMapper.updateContent(reqBody.getContent(), reqBody.getId());
            comment.setContent(reqBody.getContent());
        }
        return ModalTrans.comment(comment);
    }

    @Override
    public PageData<CommentModel> retrievePage(String pagePath, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> list = commentMapper.findAllByPagePathAndPidAndNormal(pagePath, -1);
        return pageTrans(list);
    }

    @Override
    public List<CommentModel> retrievePageByPid(long pid) {
        List<Comment> allByPagePath = commentMapper.findAllByPid(pid);
        return allByPagePath
                .stream()
                .filter(comment -> comment.getStatus() != CommentStatusEnum.DELETED.getCode())
                .map(ModalTrans::comment)
                .collect(Collectors.toList());
    }

    @Override
    public PageData<CommentModel> retrievePageByAuthor(String pagePath, int page, int count) {
        User user = redisUserUtil.get();
        PageHelper.startPage(page, count);
        List<Comment> list = commentMapper.findAllByPagePathAndFromUser(pagePath, user.getId());
        return pageTrans(list);
    }

    @Override
    public PageData<CommentModel> retrievePageByPageAndPid(String pagePath, long pid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> list = commentMapper.findAllByPagePath(pagePath);
        return pageTrans(list, true);
    }

    @Override
    public PageData<CommentModel> retrievePageByPage(String pagePath, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> list = commentMapper.findAllByPagePath(pagePath);
        return pageTrans(list, true);
    }

    private PageData<CommentModel> pageTrans(List<Comment> commentList) {
        return pageTrans(commentList, false);
    }

    private PageData<CommentModel> pageTrans(List<Comment> commentList, boolean noResponseList) {
        PageInfo<Comment> p = PageInfo.of(commentList);
        List<CommentModel> modelList = commentList
                .stream()
                .map(ModalTrans::comment)
                .peek(commentModel -> commentModel.setRespComment(this.retrievePageByPid(commentModel.getId())))
                .collect(Collectors.toList());
        return new PageData<>(p, modelList);
    }
}

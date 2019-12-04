package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.model.CommentModel;
import cn.celess.blog.entity.request.CommentReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.CommentMapper;
import cn.celess.blog.service.CommentService;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.DateFormatUtil;
import cn.celess.blog.util.RedisUserUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/29 17:05
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserService userService;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisUserUtil redisUserUtil;

    @Override
    public CommentModel create(CommentReq reqBody) {

        if (reqBody == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        long authorID = redisUserUtil.get().getId();
        Comment pComment = null;
        if (reqBody.getPid() != null && reqBody.getPid() != -1) {
            pComment = commentMapper.findCommentById(reqBody.getPid());
        }
        if (reqBody.getPid() == null) {
            reqBody.setPid(-1L);
        }
        //不是一级评论
        if (reqBody.getPid() != -1 && pComment == null) {
            //父评论不存在
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        Comment comment = new Comment();
        comment.setAuthorID(authorID);
        comment.setType(reqBody.getComment());
        if (reqBody.getComment()) {
            //若为评论
            if (reqBody.getArticleID() <= 0) {
                throw new MyException(ResponseEnum.PARAMETERS_ERROR);
            }
            comment.setArticleID(reqBody.getArticleID());
        } else {
            comment.setArticleID(-1L);
        }
        comment.setContent(reqBody.getContent());
        comment.setPid(reqBody.getPid());
        comment.setDate(new Date());
        comment.setResponseId("");
        commentMapper.insert(comment);
        if (reqBody.getPid() != -1) {
            commentMapper.updateResponder(pComment.getResponseId() + comment.getId() + ",", reqBody.getPid());
        }
        return trans(comment);
    }

    @Override
    public boolean delete(long id) {
        boolean b = commentMapper.existsById(id);
        if (!b) {
            throw new MyException(ResponseEnum.COMMENT_NOT_EXIST);
        }
        commentMapper.delete(id);
        return true;
    }

    @Override
    public CommentModel update(CommentReq reqBody) {
        if (reqBody.getId() == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "id不可为空");
        }
        Comment comment = commentMapper.findCommentById(reqBody.getId());
        if (!comment.getContent().equals(reqBody.getContent())) {
            commentMapper.updateContent(reqBody.getContent(), reqBody.getId());
            comment.setContent(reqBody.getContent());
        }
        if (!comment.getResponseId().equals(reqBody.getResponseId())) {
            commentMapper.updateResponder(reqBody.getResponseId(), reqBody.getId());
            comment.setResponseId(reqBody.getResponseId());
        }
        return trans(comment);
    }


    @Override
    public PageInfo<CommentModel> retrievePage(Boolean isComment, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findAllByType(isComment);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }

    @Override
    public PageInfo<CommentModel> retrievePageByPid(long pid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findAllByPId(pid);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }

    @Override
    public PageInfo<CommentModel> retrievePageByArticle(long articleID, long pid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findAllByArticleIDAndPId(articleID, pid);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }

    @Override
    public PageInfo<CommentModel> retrievePageByTypeAndPid(Boolean isComment, int pid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findCommentsByTypeAndPId(isComment, pid);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }

    @Override
    public PageInfo<CommentModel> retrievePageByAuthor(Boolean isComment, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findAllByAuthorIDAndType(redisUserUtil.get().getId(), isComment);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }


    @Override
    public PageInfo<CommentModel> retrievePageByType(Boolean isComment, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.findAllByType(isComment);
        PageInfo pageInfo = new PageInfo(commentList);
        pageInfo.setList(list2List(commentList));
        return pageInfo;
    }

    private List<CommentModel> list2List(List<Comment> commentList) {
        List<CommentModel> content = new ArrayList<>();
        for (Comment c : commentList) {
            content.add(trans(c));
        }
        return content;
    }

    private CommentModel trans(Comment comment) {
        CommentModel commentModel = new CommentModel();
        commentModel.setId(comment.getId());
        commentModel.setComment(comment.getType());
        commentModel.setContent(comment.getContent());
        commentModel.setArticleID(comment.getArticleID());
        commentModel.setDate(DateFormatUtil.get(comment.getDate()));
        commentModel.setResponseId(comment.getResponseId());
        commentModel.setPid(comment.getPid());
        commentModel.setAuthorName(userService.getNameById(comment.getAuthorID()));
        commentModel.setAuthorAvatarImgUrl("http://cdn.celess.cn/" + userService.getAvatarImg(comment.getAuthorID()));

        if (comment.getType() && commentModel.getArticleID() > 0) {
            commentModel.setArticleTitle(articleMapper.getTitleById(comment.getArticleID()));
        }
        return commentModel;
    }

}

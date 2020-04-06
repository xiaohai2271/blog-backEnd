package cn.celess.blog.controller;

import cn.celess.blog.entity.Comment;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.request.CommentReq;
import cn.celess.blog.service.CommentService;
import cn.celess.blog.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author : xiaohai
 * @date : 2019/03/30 20:37
 */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * 新增一条评论数据
     *
     * @param reqBody 请求数据
     * @return Response
     */
    @PostMapping("/user/comment/create")
    public Response addOne(@RequestBody CommentReq reqBody) {
        return ResponseUtil.success(commentService.create(reqBody));
    }

    @DeleteMapping("/user/comment/del")
    public Response delete(@RequestParam("id") long id) {
        return ResponseUtil.success(commentService.delete(id));
    }

    @PutMapping("/user/comment/update")
    public Response update(@RequestBody CommentReq reqBody) {
        return ResponseUtil.success(commentService.update(reqBody));
    }

    /**
     * 获取所有的一级评论
     *
     * @param articleId 文章id
     * @param count     单页数据量
     * @param page      页码
     * @return Response
     */
    @GetMapping("/comments")
    public Response commentsOfArticle(@RequestParam("articleId") long articleId,
                                      @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseUtil.success(commentService.retrievePageByArticle(articleId, -1, page, count));
    }

    /**
     * 通过pid获取数据
     *
     * @param pid
     * @return
     */
    @GetMapping("/comment/pid/{pid}")
    public Response retrievePage(@PathVariable("pid") long pid) {
        return ResponseUtil.success(commentService.retrievePageByPid(pid));
    }

    /**
     * 获取所以的一级留言
     *
     * @param count
     * @param page
     * @return
     */
    @GetMapping("/leaveMsg")
    public Response retrievePageOfLeaveMsg(@RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseUtil.success(commentService.retrievePageByTypeAndPid(false, -1, page, count));
    }

    @GetMapping("/admin/comment/type/{type}")
    public Response retrievePageAdmin(
            @PathVariable("type") int isComment,
            @RequestParam(value = "count", required = false, defaultValue = "10") int count,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseUtil.success(commentService.retrievePageByType(1 == isComment, page, count));
    }

    @GetMapping("/user/comment/type/{type}")
    public Response retrievePageByAuthor(
            @PathVariable(value = "type") int isComment,
            @RequestParam(value = "count", required = false, defaultValue = "10") int count,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        return ResponseUtil.success(commentService.retrievePageByAuthor(1 == isComment, page, count));
    }

}

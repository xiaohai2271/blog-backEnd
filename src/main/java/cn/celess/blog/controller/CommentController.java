package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.request.CommentReq;
import cn.celess.blog.service.CommentService;
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
        return Response.success(commentService.create(reqBody));
    }

    @DeleteMapping("/user/comment/del")
    public Response delete(@RequestParam("id") long id) {
        return Response.success(commentService.delete(id));
    }

    @PutMapping("/user/comment/update")
    public Response update(@RequestBody CommentReq reqBody) {
        return Response.success(commentService.update(reqBody));
    }

    /**
     * 获取所有的评论
     *
     * @param pagePath pagePath
     * @param count    单页数据量
     * @param page     页码
     * @return Response
     */
    @GetMapping("/comments/{pagePath}/{pid}")
    public Response commentsOfArticle(@PathVariable("pagePath") String pagePath, @PathVariable(value = "pid") long pid,
                                      @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        String path = "";
        if (pagePath.contains("article+")) {
            path = "article/" + pagePath.split("\\+", 2)[1];
        } else {
            path = pagePath;
        }
        if ("*".equals(pagePath)) {
            path = null;
        }
        return Response.success(commentService.retrievePageByPageAndPid(path, pid, page, count));
    }

    /**
     * 通过pid获取数据
     *
     * @param pagePath pagePath
     * @param count    count
     * @param page     page
     * @return Response
     */
    @GetMapping("/comment/pagePath/{pagePath}")
    public Response retrievePage(@PathVariable("pagePath") String pagePath,
                                 @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        String path = "";
        if (pagePath.contains("article+")) {
            path = "article/" + pagePath.split("\\+", 2)[1];
        } else {
            path = pagePath;
        }
        if ("*".equals(pagePath)) {
            path = null;
        }
        return Response.success(commentService.retrievePage(path, page, count));
    }

    @GetMapping("/user/comment/pagePath/{pagePath}")
    public Response userComment(@PathVariable("pagePath") String pagePath,
                                @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        String path = "";
        if (pagePath.contains("article+")) {
            path = "article/" + pagePath.split("\\+", 2)[1];
        } else {
            path = pagePath;
        }
        if ("*".equals(pagePath)) {
            path = null;
        }
        return Response.success(commentService.retrievePageByAuthor(path, page, count));
    }

    @GetMapping("/admin/comment/pagePath/{pagePath}")
    public Response adminComment(@PathVariable("pagePath") String pagePath,
                                 @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        String path = "";
        if (pagePath.contains("article+")) {
            path = "article/" + pagePath.split("\\+", 2)[1];
        } else {
            path = pagePath;
        }
        if ("*".equals(pagePath)) {
            path = null;
        }
        return Response.success(commentService.retrievePageByPage(path, page, count));
    }
}

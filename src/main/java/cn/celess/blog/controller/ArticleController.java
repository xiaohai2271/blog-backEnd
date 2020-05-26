package cn.celess.blog.controller;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.service.ArticleService;
import cn.celess.blog.util.RedisUserUtil;
import cn.celess.blog.util.SitemapGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:18
 */
@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    SitemapGenerateUtil sitemapGenerateUtil;
    @Autowired
    RedisUserUtil redisUserUtil;

    /**
     * 新建一篇文章
     *
     * @param body 请求数据
     * @return Response
     */
    @PostMapping("/admin/article/create")
    public Response create(@RequestBody ArticleReq body) {
        ArticleModel articleModel = articleService.create(body);
        sitemapGenerateUtil.createSitemap();
        return Response.success(articleModel);
    }

    /**
     * 通过文章id 删除一篇文章
     *
     * @param articleId 文章id
     * @return Response
     */
    @DeleteMapping("/admin/article/del")
    public Response delete(@RequestParam("articleID") long articleId) {
        boolean delete = articleService.delete(articleId);
        sitemapGenerateUtil.createSitemap();
        return Response.success(delete);
    }

    /**
     * 更新文章
     *
     * @param body 请求数据
     * @return Response
     */
    @PutMapping("/admin/article/update")
    public Response update(@RequestBody ArticleReq body) {
        ArticleModel update = articleService.update(body);
        sitemapGenerateUtil.createSitemap();
        return Response.success(update);
    }

    /**
     * 通过id查找一篇文章
     * 公开 =>返回数据
     * 不公开
     * *** =>作者 返回数据
     * *** =>其他 抛出错误
     *
     * @param articleId 文章id
     * @param is4update 是否是更新
     * @return Response
     */
    @GetMapping("/article/articleID/{articleID}")
    public Response retrieveOneById(@PathVariable("articleID") long articleId,
                                    @RequestParam(value = "update", defaultValue = "false") boolean is4update,
                                    HttpServletRequest request) {
        ArticleModel article = articleService.retrieveOneById(articleId, is4update);
        if (article.getOpen()) {
            return Response.success(article);
        } else if (article.getAuthor().getId().equals(redisUserUtil.get().getId())) {
            return Response.success(article);
        }
        return Response.response(ResponseEnum.PERMISSION_ERROR, null);
    }

    /**
     * 分页获取所有文章状态为开放的的文章
     *
     * @param page  页码
     * @param count 单页数据量
     * @return Response
     */
    @GetMapping("/articles")
    public Response articles(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "count", defaultValue = "5") int count) {
        return Response.success(articleService.retrievePageForOpen(count, page));
    }

    /**
     * 分页获取所有文章
     *
     * @param page  页码
     * @param count 单页数据量
     * @return Response
     */
    @GetMapping("/admin/articles")
    public Response adminArticles(@RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "count", defaultValue = "10") int count) {
        return Response.success(articleService.adminArticles(count, page));
    }

    /**
     * 通过分类获取文章（文章摘要）
     *
     * @param name  分类名
     * @param page  页码
     * @param count 单页数据量
     * @return Response
     */
    @GetMapping("/articles/category/{name}")
    public Response findByCategory(@PathVariable("name") String name,
                                   @RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "count", defaultValue = "10") int count) {
        return Response.success(articleService.findByCategory(name, page, count));
    }

    /**
     * 通过标签名获取文章（文章摘要）
     *
     * @param name  标签名
     * @param page  页码
     * @param count 单页数据量
     * @return Response
     */
    @GetMapping("/articles/tag/{name}")
    public Response findByTag(@PathVariable("name") String name,
                              @RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "count", defaultValue = "10") int count) {
        return Response.success(articleService.findByTag(name, page, count));
    }


    @GetMapping("/createSitemap")
    public Response createSitemap() {
        sitemapGenerateUtil.createSitemap();
        return Response.success(null);
    }
}

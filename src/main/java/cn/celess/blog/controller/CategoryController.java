package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.service.CategoryService;
import cn.celess.blog.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : xiaohai
 * @date : 2019/03/30 20:36
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 新增一个分类
     *
     * @param name 分类名
     * @return Response
     */
    @PostMapping("/admin/category/create")
    public Response addOne(@RequestParam("name") String name) {
        return ResponseUtil.success(categoryService.create(name));
    }

    /**
     * 删除一个分类
     *
     * @param id 分类id
     * @return Response
     */
    @DeleteMapping("/admin/category/del")
    public Response deleteOne(@RequestParam("id") long id) {
        return ResponseUtil.success(categoryService.delete(id));
    }

    /**
     * 更新一个分类
     *
     * @param id   分类id
     * @param name 更新后的名字
     * @return Response
     */
    @PutMapping("/admin/category/update")
    public Response updateOne(@RequestParam("id") Long id,
                              @RequestParam("name") String name) {
        return ResponseUtil.success(categoryService.update(id, name));
    }

    /**
     * 获取所有的分类
     *
     * @return Response
     */
    @GetMapping("/categories")
    public Response getPage(@RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "count", defaultValue = "1000") int count) {
        return ResponseUtil.success(categoryService.retrievePage(page, count));
    }
}

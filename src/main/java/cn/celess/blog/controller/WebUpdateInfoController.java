package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.service.WebUpdateInfoService;
import cn.celess.blog.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : xiaohai
 * @date : 2019/05/12 13:09
 */
@RestController
public class WebUpdateInfoController {
    @Autowired
    WebUpdateInfoService webUpdateInfoService;

    @PostMapping("/admin/webUpdate/create")
    public Response create(@RequestParam("info") String info) {
        return ResponseUtil.success(webUpdateInfoService.create(info));
    }

    @DeleteMapping("/admin/webUpdate/del/{id}")
    public Response del(@PathVariable("id") long id) {
        return ResponseUtil.success(webUpdateInfoService.del(id));
    }

    @PutMapping("/admin/webUpdate/update")
    public Response update(@RequestParam("id") long id, @RequestParam("info") String info) {
        return ResponseUtil.success(webUpdateInfoService.update(id, info));
    }

    @GetMapping("/webUpdate")
    public Response findAll() {
        return ResponseUtil.success(webUpdateInfoService.findAll());
    }

    @GetMapping("/webUpdate/pages")
    public Response page(@RequestParam("page") int page, @RequestParam("count") int count) {
        return ResponseUtil.success(webUpdateInfoService.pages(count, page));
    }
    @GetMapping("/lastestUpdate")
    public Response lastestUpdateTime() {
        return ResponseUtil.success(webUpdateInfoService.getLastestUpdateTime());
    }


}

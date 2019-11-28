package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.service.TagService;
import cn.celess.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/30 20:36
 */
@RestController
public class TagController {
    @Autowired
    TagService tagService;


    @PostMapping("/admin/tag/create")
    public Response addOne(@RequestParam("name") String name) {
        return ResponseUtil.success(tagService.create(name));
    }

    @DeleteMapping("/admin/tag/del")
    public Response delOne(@RequestParam("id") long id) {
        return ResponseUtil.success(tagService.delete(id));
    }


    @PutMapping("/admin/tag/update")
    public Response updateOne(@RequestParam("id") Long id, @RequestParam("name") String name) {
        return ResponseUtil.success(tagService.update(id, name));
    }

    @GetMapping("/tag/id/{id}")
    public Response retrieveOneById(@PathVariable("id") long id) {
        return ResponseUtil.success(tagService.retrieveOneById(id));
    }

    @GetMapping("/tag/name/{name}")
    public Response retrieveOneByName(@PathVariable("name") String name) {
        return ResponseUtil.success(tagService.retrieveOneByName(name));
    }

    @GetMapping("/tags")
    public Response getPage(@RequestParam(required = false, defaultValue = "10", value = "count") int count,
                            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        return ResponseUtil.success(tagService.retrievePage(page, count));
    }

    @GetMapping("/tags/nac")
    public Response getTagNameAndCount() {
        List<JSONObject> nameAndCount = new ArrayList<>();
        List<Tag> all = tagService.findAll();
        for (Tag t : all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", t.getName());
            String articles = t.getArticles();
            jsonObject.put("size", articles == null ? 0 : articles.split(",").length);
            nameAndCount.add(jsonObject);
        }
        return ResponseUtil.success(nameAndCount);
    }

}

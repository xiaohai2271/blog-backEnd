package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.TagModel;
import cn.celess.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return Response.success(tagService.create(name));
    }

    @DeleteMapping("/admin/tag/del")
    public Response delOne(@RequestParam("id") long id) {
        return Response.success(tagService.delete(id));
    }


    @PutMapping("/admin/tag/update")
    public Response updateOne(@RequestParam("id") Long id, @RequestParam("name") String name) {
        return Response.success(tagService.update(id, name));
    }

    @GetMapping("/tags")
    public Response getPage(@RequestParam(required = false, defaultValue = "10", value = "count") int count,
                            @RequestParam(required = false, defaultValue = "1", value = "page") int page) {
        return Response.success(tagService.retrievePage(page, count));
    }

    @GetMapping("/tags/nac")
    public Response getTagNameAndCount() {
        List<Map<String, Object>> nameAndCount = new ArrayList<>();
        List<TagModel> all = tagService.findAll();
        for (TagModel t : all) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("name", t.getName());
            map.put("size", t.getArticles().size());
            nameAndCount.add(map);
        }
        return Response.success(nameAndCount);
    }

}

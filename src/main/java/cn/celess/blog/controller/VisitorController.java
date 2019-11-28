package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.service.CountService;
import cn.celess.blog.service.VisitorService;
import cn.celess.blog.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaohai
 * @date : 2019/04/02 23:09
 */
@RestController
public class VisitorController {
    @Autowired
    VisitorService visitorService;
    @Autowired
    CountService countService;

    @GetMapping("/visitor/count")
    public Response getVisitorCount() {
        return ResponseUtil.success(countService.getVisitorCount());
    }

    @GetMapping("/admin/visitor/page")
    public Response page(@RequestParam(value = "count", required = false, defaultValue = "10") int count,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "showLocation", required = false, defaultValue = "false") boolean showLocation) {
        return ResponseUtil.success(visitorService.visitorPage(page, count, showLocation));
    }

    @PostMapping("/visit")
    public Response add(HttpServletRequest request) {
        return ResponseUtil.success(visitorService.addVisitor(request));
    }

    @GetMapping("/dayVisitCount")
    public Response dayVisitCount() {
        return ResponseUtil.success(countService.getDayVisitCount());
    }

    @GetMapping("/ip/{ip}")
    public Response ipLocation(@PathVariable("ip") String ip) {
        return ResponseUtil.success(visitorService.location(ip));
    }

    /**
     * 获取本地访问者的ip
     *
     * @param request
     * @return
     */
    @GetMapping("/ip")
    public Response getIp(HttpServletRequest request) {
        return ResponseUtil.success(request.getRemoteAddr());
    }
}

package cn.celess.blog.controller;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.model.QiniuResponse;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.service.CountService;
import cn.celess.blog.service.QiniuService;
import cn.celess.blog.util.HttpUtil;
import cn.celess.blog.util.RedisUserUtil;
import cn.celess.blog.util.RedisUtil;
import cn.celess.blog.util.VeriCodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : xiaohai
 * @date : 2019/04/02 22:03
 */
@RestController
public class CommonController {
    public static final Logger logger = LoggerFactory.getLogger(Object.class);

    @Autowired
    CountService countService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisUserUtil redisUserUtil;


    @GetMapping("/counts")
    public Response allCount() {
        Map<String, Long> countMap = new HashMap<>();
        countMap.put("articleCount", countService.getArticleCount());
        countMap.put("commentCount", countService.getCommentCount());
        countMap.put("categoryCount", countService.getCategoriesCount());
        countMap.put("tagCount", countService.getTagsCount());
        countMap.put("visitorCount", countService.getVisitorCount());
        return Response.success(countMap);
    }


    /**
     * 获取header的全部参数
     *
     * @param request HttpServletRequest
     * @return Response
     */
    @GetMapping("/headerInfo")
    public Response headerInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String str = null;
        while (headerNames.hasMoreElements()) {
            str = headerNames.nextElement();
            map.put(str, request.getHeader(str));
        }
        map.put("sessionID", request.getSession().getId());
        map.put("request.getRemoteAddr()", request.getRemoteAddr());
        return Response.success(map);
    }

    /**
     * 返回验证码
     *
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @GetMapping(value = "/imgCode", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object[] obj = VeriCodeUtil.createImage();
        request.getSession().setAttribute("code", obj[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) obj[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
        os.close();
    }

    /**
     * 验证 验证码的正确性
     *
     * @param code    传进来的验证码
     * @param request HttpServletRequest
     * @return Session中写入验证状态
     */
    @PostMapping("/verCode")
    public Response verCode(@RequestParam("code") String code, HttpServletRequest request) {
        request.getSession().setAttribute("verImgCodeStatus", false);
        String codeStr = (String) request.getSession().getAttribute("code");
        if (code == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        if (codeStr == null) {
            throw new MyException(ResponseEnum.IMG_CODE_TIMEOUT);
        }
        code = code.toLowerCase();
        codeStr = codeStr.toLowerCase();
        if (code.equals(codeStr)) {
            request.getSession().removeAttribute("code");
            request.getSession().setAttribute("verImgCodeStatus", true);
            return Response.success("验证成功");
        } else {
            request.getSession().removeAttribute("code");
            return Response.failure("验证失败，请重新获取验证码");
        }
    }

    /**
     * FIXME :: 单张图片多次上传的问题
     * editor.md图片上传的接口
     * FUCK !!!
     * !! 推荐使用 fileUpload(/fileUpload) 接口
     * @param file 文件
     */
    @Deprecated
    @PostMapping("/imgUpload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam("editormd-image-file") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String uploadTimesStr = redisUtil.get(request.getRemoteAddr() + "-ImgUploadTimes");
        int uploadTimes = 0;
        if (uploadTimesStr != null) {
            uploadTimes = Integer.parseInt(uploadTimesStr);
        }
        if (uploadTimes == 10) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "上传次数已达10次，请2小时后在上传");
        }
        request.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (file.isEmpty()) {
            map.put("success", 0);
            map.put("message", "上传失败，请选择文件");
            response.getWriter().println(mapper.writeValueAsString(map));
            return;
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String mime = fileName.substring(fileName.lastIndexOf("."));
        if (".png".equals(mime.toLowerCase()) || ".jpg".equals(mime.toLowerCase()) ||
                ".jpeg".equals(mime.toLowerCase()) || ".bmp".equals(mime.toLowerCase())) {
            QiniuResponse qiniuResponse = qiniuService.uploadFile(file.getInputStream(), "img_" + System.currentTimeMillis() + mime);
            map.put("success", 1);
            map.put("message", "上传成功");
            map.put("url", "http://cdn.celess.cn/" + qiniuResponse.key);
            response.getWriter().println(mapper.writeValueAsString(map));
            redisUtil.setEx(request.getRemoteAddr() + "-ImgUploadTimes", uploadTimes + 1 + "", 2, TimeUnit.HOURS);
            return;
        }
        map.put("success", 0);
        map.put("message", "上传失败，请上传图片文件");
        response.getWriter().println(mapper.writeValueAsString(map));
    }

    @GetMapping("/bingPic")
    public Response bingPic() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(HttpUtil.get("https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN"));
        } catch (IOException e) {
            return Response.failure(null);
        }
        JsonNode images = root.get("images").elements().next();
        return Response.success("https://cn.bing.com" + images.get("url").asText());
    }

    @PostMapping("/fileUpload")
    public Response<List<Map<String, Object>>> fileUpload(@RequestParam("file[]") MultipartFile[] files) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        String uploadTimesStr = redisUtil.get(redisUserUtil.get().getEmail() + "-fileUploadTimes");
        int uploadTimes = 0;
        if (uploadTimesStr != null) {
            uploadTimes = Integer.parseInt(uploadTimesStr);
        }
        if (uploadTimes == 10) {
            throw new MyException(ResponseEnum.FAILURE.getCode(), "上传次数已达10次，请2小时后在上传");
        }
        if (files.length == 0) {
            throw new MyException(ResponseEnum.NO_FILE);
        }
        for (MultipartFile file : files) {
            Map<String, Object> resp = new HashMap<>(4);

            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String mime = fileName.substring(fileName.lastIndexOf("."));
            String name = fileName.replace(mime, "").replaceAll(" ", "");
            QiniuResponse qiniuResponse = qiniuService.uploadFile(file.getInputStream(), "file_" + name + '_' + System.currentTimeMillis() + mime);
            resp.put("originalFilename", fileName);
            resp.put("success", qiniuResponse != null);
            if (qiniuResponse != null) {
                resp.put("host", "http://cdn.celess.cn/");
                resp.put("path", qiniuResponse.key);
            }
            result.add(resp);
        }
        return Response.success(result);
    }
}

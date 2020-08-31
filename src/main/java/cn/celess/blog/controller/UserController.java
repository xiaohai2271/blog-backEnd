package cn.celess.blog.controller;

import cn.celess.blog.entity.Response;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.entity.request.UserReq;
import cn.celess.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : xiaohai
 * @date : 2019/03/30 20:37
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/login")
    public Response login(@RequestBody LoginReq loginReq) {
        return Response.success(userService.login(loginReq));
    }

    @PostMapping("/registration")
    public Response registration(@RequestParam("email") String email,
                                 @RequestParam("password") String password) {
        return Response.success(userService.registration(email, password));
    }

    @GetMapping("/logout")
    public Response logout() {
        return Response.success(userService.logout());
    }

    @PutMapping("/user/userInfo/update")
    public Response updateInfo(String desc, String displayName) {
        return Response.success(userService.update(desc, displayName));
    }

    @GetMapping("/user/userInfo")
    public Response getUserInfo() {
        return Response.success(userService.getUserInfoBySession());
    }

    /**
     * 更新头像
     *
     * @param file file
     * @return
     * @throws IOException
     */
    @PostMapping("/user/imgUpload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Response.failure("上传失败，请选择文件");
        }
        String fileName = file.getOriginalFilename();
        String mime = fileName.substring(fileName.lastIndexOf("."));
        if (".png".equals(mime.toLowerCase()) || ".jpg".equals(mime.toLowerCase()) ||
                ".jpeg".equals(mime.toLowerCase()) || ".bmp".equals(mime.toLowerCase())) {
            return (Response) userService.updateUserAavatarImg(file.getInputStream(), mime);
        }
        return Response.failure("请上传图片文件");
    }

    @PostMapping("/sendResetPwdEmail")
    public Response sendResetPwdEmail(@RequestParam("email") String email) {
        return Response.success(userService.sendResetPwdEmail(email));
    }

    @PostMapping("/sendVerifyEmail")
    public Response sendVerifyEmail(@RequestParam("email") String email) {
        return Response.success(userService.sendVerifyEmail(email));
    }


    @PostMapping("/emailVerify")
    public Response emailVerify(@RequestParam("verifyId") String verifyId,
                                @RequestParam("email") String mail) {
        return Response.success(userService.verifyEmail(verifyId, mail));
    }

    @PostMapping("/resetPwd")
    public Response resetPwd(@RequestParam("verifyId") String verifyId,
                             @RequestParam("email") String email,
                             @RequestParam("pwd") String pwd) {
        return Response.success(userService.reSetPwd(verifyId, email, pwd));
    }

    @PostMapping("/user/setPwd")
    public Response setPwd(@RequestParam("pwd") String pwd,
                           @RequestParam("newPwd") String newPwd,
                           @RequestParam("confirmPwd") String confirmPwd) {
        return Response.success(userService.setPwd(pwd, newPwd, confirmPwd));
    }


    @DeleteMapping("/admin/user/delete")
    public Response multipleDelete(@RequestBody Integer[] ids) {
        return Response.success(userService.deleteUser(ids));
    }

    @DeleteMapping("/admin/user/delete/{id}")
    public Response delete(@PathVariable("id") Integer id) {
        return Response.success(userService.deleteUser(new Integer[]{id}));
    }

    @PutMapping("/admin/user")
    public Response updateInfoByAdmin(@RequestBody UserReq user) {
        return Response.success(userService.adminUpdate(user));
    }

    @GetMapping("/admin/users")
    public Response getAllUser(@RequestParam("page") int pageNum, @RequestParam("count") int count, @RequestParam(name = "status", required = false) Integer status) {
        return Response.success(userService.getUserList(pageNum, count, status));
    }

    @GetMapping("/emailStatus/{email}")
    public Response getEmailStatus(@PathVariable("email") String email) {
        return Response.success(userService.getStatusOfEmail(email));
    }


}

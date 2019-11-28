package cn.celess.blog.service;

import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.UserModel;
import cn.celess.blog.entity.request.LoginReq;
import cn.celess.blog.entity.request.UserReq;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/30 18:40
 */
@Service
public interface UserService {
    /**
     * 注册
     *
     * @param email    邮箱
     * @param password 密码
     * @return 注册状态
     */
    Boolean registration(String email, String password);

    /**
     * 登录
     *
     * @param loginReq 请求数据
     * @return 用户数据
     */
    UserModel login(LoginReq loginReq);

    /**
     * 注销登录
     *
     * @return **
     */
    Object logout();

    /**
     * 获取用户头像的链接
     *
     * @param id 用户id
     * @return 头像链接
     */
    String getAvatarImg(long id);

    /**
     * 更新用户数据
     *
     * @param desc        用户描述
     * @param displayName 显示昵称
     * @return 用户数据
     */
    UserModel update(String desc, String displayName);

    /**
     * 更新头像
     *
     * @param is   头像文件的输入流
     * @param mime 文件的mime
     * @return 响应数据
     */
    Object updateUserAavatarImg(InputStream is, String mime);

    /**
     * 获取session中存储的用户资料
     *
     * @return 用户资料
     */
    UserModel getUserInfoBySession();

    /**
     * 获取用户的角色
     *
     * @param email 用户的邮箱
     * @return role
     */
    String getUserRoleByEmail(String email);

    /**
     * 通过邮箱获取用户的信息
     *
     * @param email 用户邮箱
     * @return 用户信息
     */
    User getUserInfoByEmail(String email);

    /**
     * 获取邮箱是否注册过
     *
     * @param email 用户邮箱
     * @return 注册状态
     */
    boolean isExistOfEmail(String email);

    /**
     * 获取用户的name 优先返回displayName 否则返回email
     *
     * @param id 用户id
     * @return name
     */
    String getNameById(long id);

    /**
     * 发送重置密码邮件
     *
     * @param email 用户邮箱
     * @return 发送状态
     */
    Object sendResetPwdEmail(String email);

    /**
     * 发送验证邮箱邮件
     *
     * @param email 用户邮箱
     * @return 发送状态
     */
    Object sendVerifyEmail(String email);

    /**
     * 验证邮箱
     *
     * @param verifyId 验证码
     * @param email    邮箱
     * @return 验证状态
     */
    Object verifyEmail(String verifyId, String email);

    /**
     * 重置密码
     *
     * @param verifyId 验证码
     * @param email    邮箱
     * @param pwd      新密码
     * @return 修改状态
     */
    Object reSetPwd(String verifyId, String email, String pwd);

    /**
     * 删除用户
     *
     * @param id 用户id的数组
     * @return 对应id 的删除状态
     */
    Object deleteUser(Integer[] id);

    /**
     * 获取用户列表
     *
     * @param count 单页数据量
     * @param page  数据页
     * @return 分页数据
     */
    PageInfo<UserModel> getUserList(Integer page, Integer count);

    /**
     * 更改用户信息
     *
     * @param user 用户数据
     * @return 用户信息
     */
    UserModel adminUpdate(UserReq user);

    /**
     * 获取电子邮件的存在状态
     *
     * @param email email
     * @return true:存在  false：不存在
     */
    boolean getStatusOfEmail(String email);
}

package cn.celess.common.service;

import cn.celess.common.entity.dto.LoginReq;
import cn.celess.common.entity.dto.UserReq;
import cn.celess.common.entity.vo.PageData;
import cn.celess.common.entity.vo.UserModel;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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
     * 获取邮箱是否注册过
     *
     * @param email 用户邮箱
     * @return 注册状态
     */
    boolean isExistOfEmail(String email);

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
    PageData<UserModel> getUserList(Integer page, Integer count, Integer status);

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

    /**
     * 设置密码
     *
     * @param pwd        pwd
     * @param newPwd     newPwd
     * @param confirmPwd confirmPwd
     * @return UserModel
     */
    UserModel setPwd(String pwd, String newPwd, String confirmPwd);
}

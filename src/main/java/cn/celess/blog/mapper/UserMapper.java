package cn.celess.blog.mapper;

import cn.celess.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 小海
 * @Date： 2019/07/03 00:23
 * @Description：
 */
@Mapper
@Repository
public interface UserMapper {

    int addUser(User user);

    int updateInfo(String desc, String displayName, long id);

    int updateAvatarImgUrl(String avatarImgUrl, long id);

    int updateLoginTime(String email);

    int updateEmailStatus(String email, boolean status);

    int updatePwd(String email, String pwd);

    String getPwd(String email);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findById(long id);

    String getAvatarImgUrlById(long id);

    String getEmail(long id);

    String getDisPlayName(long id);

    String getRoleByEmail(String email);

    String getRoleById(long id);

    long count();

    int delete(long id);

    int lock(long id);

    int setUserRole(Long id, String role);

    List<User> findAll(Integer status);

    List<User> findAll();

    int update(User user);
}

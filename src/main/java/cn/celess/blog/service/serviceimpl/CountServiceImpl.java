package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.mapper.*;
import cn.celess.blog.service.CountService;
import cn.celess.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : xiaohai
 * @date : 2019/04/02 22:06
 */
@Service
public class CountServiceImpl implements CountService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VisitorMapper visitorMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public long getCommentCount() {
        return commentMapper.count();
    }

    @Override
    public long getArticleCount() {
        return articleMapper.count();
    }

    @Override
    public long getCategoriesCount() {
        return categoryMapper.count();
    }

    @Override
    public long getTagsCount() {
        return tagMapper.count();
    }

    @Override
    public long getUserCount() {
        return userMapper.count();
    }

    @Override
    public long getVisitorCount() {
        return visitorMapper.count();
    }

    @Override
    public long getDayVisitCount() {
        String dayVisitCount = redisUtil.get("dayVisitCount");
        if (dayVisitCount == null) {
            return 1;
        }
        return Integer.parseInt(dayVisitCount);
    }
}

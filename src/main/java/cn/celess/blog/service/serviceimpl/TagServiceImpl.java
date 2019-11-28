package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.TagMapper;
import cn.celess.blog.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : xiaohai
 * @date : 2019/03/28 22:29
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public Tag create(String name) {
        boolean b = tagMapper.existsByName(name);
        if (b) {
            throw new MyException(ResponseEnum.TAG_HAS_EXIST);
        }
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag create(Tag tag) {
        if (tag == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public boolean delete(long tagId) {
        Tag tag = tagMapper.findTagById(tagId);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        if (tag.getArticles()==null){
            return tagMapper.delete(tagId) == 1;
        }
        String[] articleArray = tag.getArticles().split(",");
        for (int i = 0; i < articleArray.length; i++) {
            if (articleArray[i] == null || "".equals(articleArray)) {
                continue;
            }
            long articleID = Long.parseLong(articleArray[i]);
            Article article = articleMapper.findArticleById(articleID);
            if (article == null) {
                continue;
            }
            article.setTagsId(article.getTagsId().replace(tagId + ",", ""));
            articleMapper.update(article);
        }
        return tagMapper.delete(tagId) == 1;
    }


    @Override
    public Tag update(Long id,String name) {
        if (id == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "缺少ID");
        }
        Tag tagFromDB = tagMapper.findTagById(id);
        tagFromDB.setName(name);

        tagMapper.update(tagFromDB);
        return tagFromDB;

    }

    @Override
    public Tag retrieveOneById(long tagId) {
        Tag tag = tagMapper.findTagById(tagId);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        return tag;
    }

    @Override
    public Tag retrieveOneByName(String name) {
        Tag tag = tagMapper.findTagByName(name);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        return tag;
    }

    @Override
    public PageInfo<Tag> retrievePage(int page, int count) {
        PageHelper.startPage(page, count);
        PageInfo pageInfo = new PageInfo(tagMapper.findAll());
        return pageInfo;
    }

    @Override
    public List<Tag> findAll() {
        return tagMapper.findAll();
    }
}

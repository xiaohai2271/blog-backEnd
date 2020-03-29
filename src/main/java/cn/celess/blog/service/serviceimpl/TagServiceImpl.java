package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.entity.model.TagModel;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.TagMapper;
import cn.celess.blog.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public TagModel create(String name) {
        boolean b = tagMapper.existsByName(name);
        if (b) {
            throw new MyException(ResponseEnum.TAG_HAS_EXIST);
        }
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insert(tag);
        return new TagModel(tag);
    }

    @Override
    public TagModel create(Tag tag) {
        if (tag == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        tagMapper.insert(tag);
        return new TagModel(tag);
    }

    @Override
    public boolean delete(long tagId) {
        Tag tag = tagMapper.findTagById(tagId);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        if (tag.getArticles() == null) {
            return tagMapper.delete(tagId) == 1;
        }
        String[] articleArray = tag.getArticles().split(",");
        for (int i = 0; i < articleArray.length; i++) {
            if (articleArray[i] == null || "".equals(articleArray[i])) {
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
    public TagModel update(Long id, String name) {
        if (id == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "缺少ID");
        }
        Tag tagFromDB = tagMapper.findTagById(id);
        tagFromDB.setName(name);

        tagMapper.update(tagFromDB);
        return new TagModel(tagFromDB);

    }

    @Override
    public TagModel retrieveOneById(long tagId) {
        Tag tag = tagMapper.findTagById(tagId);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        return new TagModel(tag);
    }

    @Override
    public TagModel retrieveOneByName(String name) {
        Tag tag = tagMapper.findTagByName(name);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        return new TagModel(tag);
    }

    @Override
    public PageInfo<TagModel> retrievePage(int page, int count) {
        PageHelper.startPage(page, count);
        List<Tag> tagList = tagMapper.findAll();
        PageInfo pageInfo = new PageInfo(tagList);
        List<TagModel> list = new ArrayList<>();
        tagList.forEach(e -> list.add(new TagModel(e)));
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<TagModel> findAll() {
        List<TagModel> list = new ArrayList<>();
        tagMapper.findAll().forEach(e -> list.add(new TagModel(e)));
        return list;
    }
}

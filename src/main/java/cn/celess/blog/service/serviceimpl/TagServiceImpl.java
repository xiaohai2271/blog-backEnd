package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.ArticleTag;
import cn.celess.blog.entity.Tag;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.model.TagModel;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.ArticleTagMapper;
import cn.celess.blog.mapper.TagMapper;
import cn.celess.blog.service.TagService;
import cn.celess.blog.util.ModalTrans;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    ArticleTagMapper articleTagMapper;

    @Override
    public TagModel create(String name) {
        boolean b = tagMapper.existsByName(name);
        if (b) {
            throw new MyException(ResponseEnum.TAG_HAS_EXIST);
        }
        Tag tag = new Tag();
        tag.setName(name);
        tagMapper.insert(tag);
        return ModalTrans.tag(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(long tagId) {
        Tag tag = tagMapper.findTagById(tagId);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        List<ArticleTag> articleByTag = articleTagMapper.findArticleByTag(tagId);
        // 删除文章
        articleByTag.forEach(articleTag -> articleMapper.delete(articleTag.getArticle().getId()));
        return tagMapper.delete(tagId) == 1;
    }


    @Override
    public TagModel update(Long id, String name) {
        if (id == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "缺少ID");
        }
        Tag tag = tagMapper.findTagById(id);
        tag.setName(name);
        tagMapper.update(tag);
        return ModalTrans.tag(tag);

    }

    @Override
    public PageData<TagModel> retrievePage(int page, int count) {
        PageHelper.startPage(page, count);
        List<Tag> tagList = tagMapper.findAll();
        List<TagModel> modelList = new ArrayList<>();
        tagList.forEach(tag -> modelList.add(ModalTrans.tag(tag)));
        return new PageData<>(new PageInfo<>(tagList), modelList);
    }

    @Override
    public List<TagModel> findAll() {
        return tagMapper.findAll().stream()
                .map(ModalTrans::tag)
                .peek(tagModel -> {
                    List<ArticleTag> articleByTagAndOpen = articleTagMapper.findArticleByTagAndOpen(tagModel.getId());
                    tagModel.setArticles(
                            articleByTagAndOpen
                                    .stream()
                                    .map(articleTag -> ModalTrans.article(articleTag.getArticle(), true))
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
    }
}

package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.LevelEnum;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.entity.*;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.ArticleMapper;
import cn.celess.blog.mapper.CategoryMapper;
import cn.celess.blog.mapper.CommentMapper;
import cn.celess.blog.mapper.TagMapper;
import cn.celess.blog.service.ArticleService;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.DateFormatUtil;
import cn.celess.blog.util.RedisUserUtil;
import cn.celess.blog.util.RegexUtil;
import cn.celess.blog.util.StringFromHtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youbenzi.mdtool.tool.MDTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author : xiaohai
 * @date : 2019/03/28 15:21
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    public static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    TagMapper tagMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisUserUtil redisUserUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleModel create(ArticleReq reqBody) {
        if (reqBody == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //数据判断
        if (reqBody.getTitle() == null || reqBody.getTitle().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        } else if (reqBody.getMdContent() == null || reqBody.getMdContent().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //转载 判断链接
        if (!reqBody.getType()) {
            if (reqBody.getUrl() == null || reqBody.getUrl().replaceAll(" ", "").isEmpty()) {
                throw new MyException(ResponseEnum.PARAMETERS_ERROR);
            } else if (!RegexUtil.urlMatch(reqBody.getUrl())) {
                throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
            }
        }
        if (reqBody.getCategory() == null || reqBody.getCategory().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        if (reqBody.getTags() == null || reqBody.getTags().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }


        //写入数据库的数据
        Article article = new Article();
        article.setTitle(reqBody.getTitle());
        article.setOpen(reqBody.getOpen());
        article.setMdContent(reqBody.getMdContent());
        article.setUrl(reqBody.getUrl());
        article.setType(reqBody.getType());

        article.setAuthorId(redisUserUtil.get().getId());
        article.setPublishDate(new Date());

        //防止出现 “null,xxx”这种情况
        article.setTagsId("");


        //是否需要更新上一篇文章
        boolean isUpdatePreArticle = true;

        Article preArticle = null;


        if (articleMapper.count() == 0) {
            isUpdatePreArticle = false;


        } else {
            //获取最新的一条数据
            preArticle = articleMapper.getLastestArticle();
        }

        if (isUpdatePreArticle) {
            logger.info("上一篇文章的id为：" + preArticle.getId());
            //设置上一篇文章的id
            article.setPreArticleId(preArticle.getId());
        }

        //markdown->html->summary
        String str = StringFromHtmlUtil.getString(MDTool.markdown2Html(article.getMdContent()));
        //获取摘要  摘要长度为255个字符
        String summary = str.length() > 240 ? str.substring(0, 240) + "......" : str;

        //去除转换后存在的空格
        String tagStr = reqBody.getTags().replaceAll(" ", "");
        article.setSummary(summary);

        if (articleMapper.existsByTitle(article.getTitle())) {
            throw new MyException(ResponseEnum.ARTICLE_HAS_EXIST);
        }


        //将分类写入数据库
        Category category1 = categoryMapper.findCategoryByName(reqBody.getCategory());
        if (category1 == null) {
            category1 = new Category();
            category1.setArticles("");
            category1.setName(reqBody.getCategory());
            categoryMapper.insert(category1);
        }

        article.setCategoryId(category1.getId());

        //文章存数据库
        articleMapper.insert(article);
        //获取新增的文章

        if (isUpdatePreArticle) {
            //更新上一篇文章的“下一篇文章ID”
            articleMapper.updateNextArticleId(preArticle.getId(), article.getId());
        }

        //无效
        // articleMapper.updatePreArticleId(article.getId(), preArticle == null ? -1 : preArticle.getId());
        article.setPreArticleId(preArticle == null ? -1 : preArticle.getId());

        category1.setArticles(category1.getArticles() + article.getId() + ",");
        categoryMapper.update(category1);


        //将标签写入数据库
        for (String t : tagStr.split(",")) {
            if (t.replaceAll(" ", "").length() == 0) {
                //单个标签只含空格
                continue;
            }
            Tag tag = tagMapper.findTagByName(t);
            if (tag == null) {
                tag = new Tag();
                tag.setName(t);
                tag.setArticles("");
                tagMapper.insert(tag);
            }
            tag.setArticles(tag.getArticles() + article.getId() + ",");
            article.setTagsId(article.getTagsId() + tag.getId() + ",");
            tagMapper.update(tag);
        }
        articleMapper.update(article);
        return fullTransform(articleMapper.getLastestArticle());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(long articleID) {

        Article articleForDel = articleMapper.findArticleById(articleID);

        if (articleForDel == null) {
            throw new MyException(ResponseEnum.ARTICLE_NOT_EXIST);//文章不存在
        }

        Article preArticle = articleMapper.findArticleById(articleForDel.getPreArticleId());
        Article nextArticle = articleMapper.findArticleById(articleForDel.getNextArticleId());

        //对访问情况进行判断  非博主/非自己文章 拒绝访问
        User user = redisUserUtil.get();
        if (!user.getRole().contains("admin") && !articleForDel.getAuthorId().equals(user.getId())) {
            throw new MyException(ResponseEnum.PERMISSION_ERROR);
        }

        //删除的文章处于中间位置
        if (nextArticle != null && preArticle != null) {

            //修改上一篇文章的“下一篇文章”y
            articleMapper.updateNextArticleId(articleForDel.getPreArticleId(), articleForDel.getNextArticleId());

            //修改下一篇文章的 “上一篇文章”
            articleMapper.updatePreArticleId(articleForDel.getNextArticleId(), articleForDel.getPreArticleId());
        }
        if (preArticle == null && nextArticle != null) {
            //删除的是第一篇文章
            articleMapper.updatePreArticleId(nextArticle.getId(), -1);
        }
        if (nextArticle == null && preArticle != null) {
            //删除的是最后一篇文章
            articleMapper.updateNextArticleId(preArticle.getId(), -1);
        }
        // delete count 为删除的数据数量
        int deleteCount = commentMapper.deleteByArticleId(articleID);

        //删除标签中的文章id
        String tag = articleForDel.getTagsId();
        if (tag.length() > 0) {
            String[] tags = tag.split(",");
            for (String t : tags) {
                if (t != null) {
                    //查询标签
                    Tag tag1 = tagMapper.findTagById(Long.parseLong(t));
                    //去除标签中的articleId中的待删除的文章id
                    String s = tag1.getArticles().replaceAll(articleForDel.getId() + ",", "");
                    tag1.setArticles(s);
                    tagMapper.update(tag1);
                }
            }
        }


        //删除分类中的文章id
        //获取文章的分类
        long categoryId = articleForDel.getCategoryId();
        Category category = categoryMapper.findCategoryById(categoryId);
        //删除文章id
        category.setArticles(category.getArticles().replaceAll(articleForDel.getId() + ",", ""));
        //更新
        categoryMapper.update(category);

        //删除指定文章
        articleMapper.delete(articleID);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleModel update(ArticleReq reqBody) {
        if (reqBody == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //数据判断
        if (reqBody.getTitle() == null || reqBody.getTitle().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        } else if (reqBody.getMdContent() == null || reqBody.getMdContent().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        //转载 判断链接
        if (!reqBody.getType()) {
            if (reqBody.getUrl() == null || reqBody.getUrl().replaceAll(" ", "").isEmpty()) {
                throw new MyException(ResponseEnum.PARAMETERS_ERROR);
            } else if (!RegexUtil.urlMatch(reqBody.getUrl())) {
                throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
            }
        }
        if (reqBody.getCategory() == null || reqBody.getCategory().replaceAll(" ", "").isEmpty()) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
//        暂时不更新tags
//        if (reqBody.getTags() == null || reqBody.getTags().replaceAll(" ", "").isEmpty()) {
//            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
//        }

        //写入数据库的数据
        Article article = new Article();
        if (reqBody.getId() == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR.getCode(), "id不能为空");
        }
        article.setId(reqBody.getId());
        article.setTitle(reqBody.getTitle());
        article.setOpen(reqBody.getOpen());
        article.setMdContent(reqBody.getMdContent());
        article.setUrl(reqBody.getUrl());
        article.setType(reqBody.getType());


        Article oldArticle = articleMapper.findArticleById(reqBody.getId());

        Category category = categoryMapper.findCategoryById(oldArticle.getCategoryId());
        if (!(category.getName()).equals(reqBody.getCategory())) {
            //修改更新之前数据 的分类
            category.setArticles(category.getArticles().replace(reqBody.getId() + ",", ""));
            //更新
            categoryMapper.update(category);

            //更新 更新之后的分类
            Category category1 = categoryMapper.findCategoryByName(reqBody.getCategory());
            if (category1 == null) {
                category1 = new Category();
                category1.setName(reqBody.getCategory());
                category1.setArticles(reqBody.getId() + ",");
                categoryMapper.insert(category1);
            }
            article.setCategoryId(category1.getId());
        } else {
            article.setCategoryId(oldArticle.getCategoryId());
        }


//        String[] newTags = reqBody.getTags().replaceAll(" ", "").split(",");

//        //防止出现 ‘null2’这种情况
//        article.setTagsId("");
//        for (String t : newTags) {
//            Tag tag = tagMapper.findTagByName(t);
//            if (tag == null) {
//                tag = new Tag();
//                tag.setName(t);
//                tag.setArticles(oldArticle.getId() + ",");
//                tag = tagMapper.save(tag);
//                article.setTagsId(article.getTagsId() + tag.getId() + ",");
//                continue;
//            }
//            article.setTagsId(article.getTagsId() + tag.getId() + ",");
//        }

        // TODO:::: tag的更新
        article.setTagsId(oldArticle.getTagsId());


        article.setUpdateDate(new Date());
        // TODO::::换用beansUtil
        // 设置不定参数
        article.setReadingNumber(oldArticle.getReadingNumber());
        article.setPublishDate(oldArticle.getPublishDate());
        article.setAuthorId(redisUserUtil.get().getId());
        article.setPreArticleId(oldArticle.getPreArticleId());
        article.setNextArticleId(oldArticle.getNextArticleId());
        String str = StringFromHtmlUtil.getString(MDTool.markdown2Html(article.getMdContent()));
        article.setSummary(str.length() > 240 ? str.substring(0, 240) + "......" : str);
        articleMapper.update(article);
        //更新完成移除
        request.getSession().removeAttribute("article4update");
        return fullTransform(article);
    }

    @Override
    public ArticleModel retrieveOneByID(long articleID, boolean is4update) {
        Article article = articleMapper.findArticleById(articleID);
        if (article == null) {
            throw new MyException(ResponseEnum.ARTICLE_NOT_EXIST);
        }
        if (!article.getOpen()) {
            User user = redisUserUtil.getWithOutExc();
            if (user == null || "user".equals(user.getRole())) {
                throw new MyException(ResponseEnum.ARTICLE_NOT_PUBLIC);
            }
        }
        article.setReadingNumber(article.getReadingNumber() + 1);
        if (is4update) {
            //因更新而获取文章  不需要增加阅读量
            request.getSession().setAttribute("article4update", article);
            return fullTransform(article);
        }
        articleMapper.setReadingNumber(article.getReadingNumber() + 1, articleID);
        return fullTransform(article);
    }

    /**
     * @param count 数目
     * @param page  页面 默认减1
     * @return
     */
    @Override
    public PageInfo adminArticles(int count, int page) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.findAll();
        PageInfo pageInfo = new PageInfo(articleList);
        pageInfo.setList(list2list(articleList, LevelEnum.BETWEEN_M_AND_H));
        return pageInfo;
    }

    @Override
    public PageInfo retrievePageForOpen(int count, int page) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.findAllByOpen(true);
        PageInfo pageInfo = new PageInfo(articleList);
        pageInfo.setList(list2list(articleList, LevelEnum.MIDDLE));
        return pageInfo;
    }

    @Override
    public PageInfo findByCategory(String name, int page, int count) {
        Long idByName = categoryMapper.getIDByName(name);
        if (idByName == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        PageHelper.startPage(page, count);
        PageInfo pageInfo = new PageInfo(articleMapper.getSimpleInfoByCategory(idByName));
        return pageInfo;
    }

    @Override
    public PageInfo findByTag(String name, int page, int count) {
        Tag tag = tagMapper.findTagByName(name);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        PageHelper.startPage(page, count);
        String[] split = tag.getArticles().split(",");
        List<String> list = Arrays.asList(split);
        List<Article> articleList = articleMapper.getSimpleInfoByTag(list);
        PageInfo pageInfo = new PageInfo(articleList);
        return pageInfo;
    }

    /**
     * page转换
     *
     * @param articleList 数据源
     * @param level       转换级别
     * @return list
     */
    private List<ArticleModel> list2list(List<Article> articleList, LevelEnum level) {
        List<ArticleModel> content = new ArrayList<>();
        for (Article a : articleList) {
            ArticleModel model;
            switch (level.getLevelCode()) {
                case 0:
                    model = simpleTransform(a);
                    break;
                case 1:
                    model = suitableTransform(a);
                    break;
                case 2:
                    model = suitableTransformForAdmin(a);
                    break;
                case 3:
                default:
                    model = fullTransform(a);
            }
            content.add(model);
        }
        return content;
    }

    /**
     * 简单的模型转换
     * [id,title,summary]
     *
     * @param a 源数据
     * @return 模型
     */
    private ArticleModel simpleTransform(Article a) {
        ArticleModel model = new ArticleModel();
        model.setId(a.getId());
        model.setTitle(a.getTitle());
        model.setSummary(a.getSummary());

        return model;
    }

    /**
     * 中等转换
     * [id,title,summary]
     * +
     * [original,tags,category]
     *
     * @param a
     * @return
     */
    private ArticleModel suitableTransform(Article a) {
        ArticleModel model = simpleTransform(a);
        model.setAuthorName(userService.getNameById(a.getAuthorId()));
        model.setPublishDateFormat(DateFormatUtil.get(a.getPublishDate()));
        model.setOriginal(a.getType());
        model.setCategory(categoryMapper.getNameById(a.getCategoryId()));
        String[] split = a.getTagsId().split(",");
        String[] tags = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            if (split[i] == null || "".equals(split[i])) {
                continue;
            }
            tags[i] = tagMapper.getNameById(Long.parseLong(split[i]));
        }
        model.setTags(tags);
        return model;
    }

    /**
     * 中等转换 for admin页面
     * [id,title]
     * +
     * [original,UpdateDate,open,readingNumber]
     *
     * @param a
     * @return
     */
    private ArticleModel suitableTransformForAdmin(Article a) {
        ArticleModel model = simpleTransform(a);
        model.setPublishDateFormat(DateFormatUtil.get(a.getPublishDate()));
        model.setUpdateDateFormat(DateFormatUtil.get(a.getUpdateDate()));
        model.setReadingNumber(a.getReadingNumber());
        model.setOpen(a.getOpen());
        model.setOriginal(a.getType());
        model.setSummary(null);
        return model;
    }

    /**
     * 全转换
     * [id,title,summary,original,tags,category]
     * +
     * [UpdateDate,MdContent,NextArticleId,NextArticleTitle,preArticleId,preArticleTitle,open,url,readingNumber]
     *
     * @param a
     * @return
     */
    private ArticleModel fullTransform(Article a) {
        ArticleModel model = suitableTransform(a);
        model.setUpdateDateFormat(DateFormatUtil.get(a.getUpdateDate()));
        model.setMdContent(a.getMdContent());
        model.setNextArticleId(a.getNextArticleId());
        model.setNextArticleTitle(a.getNextArticleId() == -1 ? "无" : articleMapper.getTitleById(a.getNextArticleId()));
        model.setPreArticleId(a.getPreArticleId());
        model.setPreArticleTitle(a.getPreArticleId() == -1 ? "无" : articleMapper.getTitleById(a.getPreArticleId()));
        model.setOpen(a.getOpen());
        model.setUrl(a.getUrl());
        model.setReadingNumber(a.getReadingNumber());
        return model;
    }

}

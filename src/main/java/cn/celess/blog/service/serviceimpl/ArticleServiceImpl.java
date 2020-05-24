package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.LevelEnum;
import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.enmu.RoleEnum;
import cn.celess.blog.entity.*;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.*;
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
    ArticleTagMapper articleTagMapper;
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
        if (reqBody.getTags() == null || reqBody.getTags().length == 0) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        if (articleMapper.existsByTitle(reqBody.getTitle())) {
            throw new MyException(ResponseEnum.ARTICLE_HAS_EXIST);
        }
        // 查看是否存在已有的分类
        Category category = (Category) categoryMapper.findCategoryByName(reqBody.getCategory());
        if (category == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }


        // 构建 需要写入数据库的对象数据
        Article article = new Article();
        article.setTitle(reqBody.getTitle());
        article.setOpen(reqBody.getOpen());
        article.setMdContent(reqBody.getMdContent());
        article.setUrl(reqBody.getUrl());
        article.setType(reqBody.getType());

        article.setUser(redisUserUtil.get());
        article.setPublishDate(new Date());

        //markdown->html->summary
        String str = StringFromHtmlUtil.getString(MDTool.markdown2Html(article.getMdContent()));
        //获取摘要  摘要长度为255个字符
        String summary = str.length() > 240 ? str.substring(0, 240) + "......" : str;
        article.setSummary(summary);

        article.setCategory(category);

        //文章存数据库
        articleMapper.insert(article);

        //将标签写入数据库
        for (String tagName : reqBody.getTags()) {
            if (tagName.replaceAll(" ", "").length() == 0) {
                //单个标签只含空格
                continue;
            }
            Tag tag = (Tag) tagMapper.findTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagMapper.insert(tag);
            }
            ArticleTag articleTag = new ArticleTag(article, tag);
            articleTagMapper.insert(articleTag);
        }
        return fullTransform(article);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(long articleId) {
        Article articleForDel = articleMapper.findArticleById(articleId);

        if (articleForDel == null) {
            //文章不存在
            throw new MyException(ResponseEnum.ARTICLE_NOT_EXIST);
        }

        //对访问情况进行判断  非admin 权限不可删除文章
        User user = redisUserUtil.get();
        if (!RoleEnum.ADMIN_ROLE.getRoleName().equals(user.getRole())) {
            throw new MyException(ResponseEnum.PERMISSION_ERROR);
        }
        //删除指定文章
        articleMapper.delete(articleId);

        articleTagMapper.deleteByArticleId(articleId);

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleModel update(ArticleReq reqBody) {
        if (reqBody == null || reqBody.getId() == null) {
            throw new MyException(ResponseEnum.PARAMETERS_ERROR);
        }
        // 查找数据
        Article article = articleMapper.findArticleById(reqBody.getId());

        //数据判断
        if (reqBody.getTitle() != null && !reqBody.getTitle().replaceAll(" ", "").isEmpty()) {
            if (articleMapper.existsByTitle(reqBody.getTitle())) {
                throw new MyException(ResponseEnum.ARTICLE_HAS_EXIST);
            }
            article.setTitle(reqBody.getTitle());
        }
        if (reqBody.getMdContent() != null && !reqBody.getMdContent().replaceAll(" ", "").isEmpty()) {
            article.setMdContent(reqBody.getMdContent());
        }

        //转载 判断链接
        if (reqBody.getType() != null) {
            if (!reqBody.getType() && reqBody.getUrl() == null) {
                throw new MyException(ResponseEnum.PARAMETERS_ERROR);
            }

            if (!RegexUtil.urlMatch(reqBody.getUrl())) {
                throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
            }
            article.setType(reqBody.getType());
            article.setUrl(reqBody.getUrl());
        }
        if (reqBody.getCategory() != null && !reqBody.getCategory().replaceAll(" ", "").isEmpty()) {
            Category category = (Category) categoryMapper.findCategoryByName(reqBody.getCategory());
            if (category == null) {
                category = new Category();
                category.setName(reqBody.getCategory());
                categoryMapper.insert(category);
            }
            article.setCategory(category);
        }

        if (reqBody.getTags() != null && reqBody.getTags().length != 0) {

        }

        //写入数据库的数据
        article.setOpen(reqBody.getOpen() ? article.getOpen() : reqBody.getOpen());
        String str = StringFromHtmlUtil.getString(MDTool.markdown2Html(article.getMdContent()));
        article.setSummary(str.length() > 240 ? str.substring(0, 240) + "......" : str);
        articleMapper.update(article);


        List<ArticleTag> allByArticleId = articleTagMapper.findAllByArticleId(article.getId());
        List<ArticleTag> updateList = new ArrayList<>();
        List<ArticleTag> deleteList = new ArrayList<>();

        for (String tag : reqBody.getTags()) {
            boolean contain = allByArticleId.stream().anyMatch(articleTag -> articleTag.getTag().getName().equals(tag));
            if (!contain) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticle(article);
                Tag tagByName = (Tag) tagMapper.findTagByName(tag);
                if (tagByName == null) {
                    tagByName = new Tag(tag);
                    tagMapper.insert(tagByName);
                }
                articleTag.setTag(tagByName);
                updateList.add(articleTag);
            }
        }

        allByArticleId.forEach(articleTag -> {
            boolean contain = false;
            for (String tag : reqBody.getTags()) {
                if (articleTag.getTag().getName().equals(tag)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                deleteList.add(articleTag);
            }
        });

        if (updateList.size() != 0) {
            updateList.forEach(articleTag -> articleTagMapper.insert(articleTag));
        }

        if (deleteList.size() != 0) {
            articleTagMapper.deleteMultiById(deleteList);
        }

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
        Long idByName = categoryMapper.getIdByName(name);
        if (idByName == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        PageHelper.startPage(page, count);
        PageInfo pageInfo = new PageInfo(articleMapper.getSimpleInfoByCategory(idByName));
        return pageInfo;
    }

    @Override
    public PageInfo findByTag(String name, int page, int count) {
        Tag tag = (Tag) tagMapper.findTagByName(name);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        // TODO :
        PageHelper.startPage(page, count);
        List<String> list = Arrays.asList(null);
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
//        model.setAuthor(a.getUser());
//        model.setPublishDateFormat(DateFormatUtil.get(a.getPublishDate()));
//        model.setOriginal(a.getType());
//        model.setCategory(categoryMapper.getNameById(a.getCategoryId()));
//        String[] split = a.getTagsId().split(",");
//        String[] tags = new String[split.length];
//        for (int i = 0; i < split.length; i++) {
//            if (split[i] == null || "".equals(split[i])) {
//                continue;
//            }
//            tags[i] = tagMapper.getNameById(Long.parseLong(split[i]));
//        }
//        model.setTags(tags);
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
//        model.setPublishDateFormat(DateFormatUtil.get(a.getPublishDate()));
//        model.setUpdateDateFormat(DateFormatUtil.get(a.getUpdateDate()));
//        model.setReadingNumber(a.getReadingNumber());
//        model.setOpen(a.getOpen());
//        model.setOriginal(a.getType());
//        model.setSummary(null);
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
//        model.setUpdateDateFormat(DateFormatUtil.get(a.getUpdateDate()));
//        model.setMdContent(a.getMdContent());
//        model.setNextArticleId(a.getNextArticleId());
//        model.setNextArticleTitle(a.getNextArticleId() == -1 ? "无" : articleMapper.getTitleById(a.getNextArticleId()));
//        model.setPreArticleId(a.getPreArticleId());
//        model.setPreArticleTitle(a.getPreArticleId() == -1 ? "无" : articleMapper.getTitleById(a.getPreArticleId()));
//        model.setOpen(a.getOpen());
//        model.setUrl(a.getUrl());
//        model.setReadingNumber(a.getReadingNumber());
        return model;
    }

}

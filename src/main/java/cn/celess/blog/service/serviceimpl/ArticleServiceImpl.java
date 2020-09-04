package cn.celess.blog.service.serviceimpl;

import cn.celess.blog.enmu.ResponseEnum;
import cn.celess.blog.enmu.RoleEnum;
import cn.celess.blog.entity.*;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.PageData;
import cn.celess.blog.entity.request.ArticleReq;
import cn.celess.blog.exception.MyException;
import cn.celess.blog.mapper.*;
import cn.celess.blog.service.ArticleService;
import cn.celess.blog.service.UserService;
import cn.celess.blog.util.ModalTrans;
import cn.celess.blog.util.RedisUserUtil;
import cn.celess.blog.util.RegexUtil;
import cn.celess.blog.util.StringFromHtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youbenzi.mdtool.tool.MDTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author : xiaohai
 * @date : 2019/03/28 15:21
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

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
        Category category = categoryMapper.findCategoryByName(reqBody.getCategory());
        if (category == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }

        // 构建 需要写入数据库的对象数据
        Article article = new Article();
        BeanUtils.copyProperties(reqBody, article);

        article.setUser(redisUserUtil.get());

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
            Tag tag = tagMapper.findTagByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagMapper.insert(tag);
            }
            ArticleTag articleTag = new ArticleTag(article, tag);
            articleTagMapper.insert(articleTag);
        }
        Article articleFromDb = articleMapper.findArticleById(article.getId());

        ArticleModel articleModel = ModalTrans.article(articleFromDb);
        articleModel.setPreArticle(ModalTrans.article(articleMapper.getPreArticle(article.getId()), true));
        return articleModel;
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

        //articleTagMapper.deleteByArticleId(articleId);

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
            if (!article.getTitle().equals(reqBody.getTitle()) && articleMapper.existsByTitle(reqBody.getTitle())) {
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

            if (!reqBody.getType() && !RegexUtil.urlMatch(reqBody.getUrl())) {
                throw new MyException(ResponseEnum.PARAMETERS_URL_ERROR);
            }
            article.setType(reqBody.getType());
            article.setUrl(reqBody.getUrl());
        }
        if (reqBody.getCategory() != null && !reqBody.getCategory().replaceAll(" ", "").isEmpty()) {
            Category category = categoryMapper.findCategoryByName(reqBody.getCategory());
            if (category == null) {
                category = new Category();
                category.setName(reqBody.getCategory());
                categoryMapper.insert(category);
            }
            article.setCategory(category);
        }

        //写入数据库的数据
        article.setOpen(reqBody.getOpen() == null ? article.getOpen() : reqBody.getOpen());
        String str = StringFromHtmlUtil.getString(MDTool.markdown2Html(article.getMdContent()));
        article.setSummary(str.length() > 240 ? str.substring(0, 240) + "......" : str);
        articleMapper.update(article);


        List<ArticleTag> allByArticleId = articleTagMapper.findAllByArticleId(article.getId());
        List<ArticleTag> updateList = new ArrayList<>();
        List<ArticleTag> deleteList = new ArrayList<>();

        // 获取要更新 的标签
        for (String tag : reqBody.getTags()) {
            boolean contain = allByArticleId.stream().anyMatch(articleTag -> articleTag.getTag().getName().equals(tag));
            if (!contain) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticle(article);
                Tag tagByName = tagMapper.findTagByName(tag);
                if (tagByName == null) {
                    tagByName = new Tag(tag);
                    tagMapper.insert(tagByName);
                }
                articleTag.setTag(tagByName);
                updateList.add(articleTag);
            }
        }
        // 获取要删除的标签
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
        ArticleModel articleModel = ModalTrans.article(articleMapper.findArticleById(article.getId()));
        setPreAndNextArticle(articleModel);
        return articleModel;
    }

    @Override
    public ArticleModel retrieveOneById(long articleId, boolean is4update) {
        Article article = articleMapper.findArticleById(articleId);
        if (article == null) {
            throw new MyException(ResponseEnum.ARTICLE_NOT_EXIST);
        }
        if (!article.getOpen()) {
            User user = redisUserUtil.getWithOutExc();
            if (user == null || "user".equals(user.getRole())) {
                throw new MyException(ResponseEnum.ARTICLE_NOT_PUBLIC);
            }
        }
        ArticleModel articleModel = ModalTrans.article(article);

        if (is4update) {
            //因更新而获取文章  不需要增加阅读量
            request.getSession().setAttribute("article4update", article);
            return articleModel;
        }
        setPreAndNextArticle(articleModel);
        articleMapper.updateReadingNumber(articleId);
        return articleModel;
    }

    /**
     * @param count 数目
     * @param page  页面
     * @return PageInfo
     */
    @Override
    public PageData<ArticleModel> adminArticles(int count, int page, Boolean deleted) {
        List<Article> articleList = articleMapper.findAll();

        PageData<ArticleModel> pageData = new PageData<>(null, 0, count, page);
        List<Article> collect;
        if (deleted != null) {
            collect = articleList.stream().filter(article -> article.isDeleted() == deleted).collect(Collectors.toList());
        } else {
            collect = articleList;
        }
        pageData.setTotal(collect.size());
        List<ArticleModel> articleModels = collect.stream()
                .peek(article -> article.setMdContent(null))
                .map(ModalTrans::article)
                .skip((page - 1) * count)
                .limit(count)
                .collect(Collectors.toList());
        pageData.setList(articleModels);

        return pageData;
    }

    @Override
    public PageData<ArticleModel> retrievePageForOpen(int count, int page) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.findAllByOpen(true);
        PageData<ArticleModel> pageData = new PageData<>(new PageInfo<Article>(articleList));

        List<ArticleModel> articleModelList = articleList
                .stream()
                .map(article -> setPreAndNextArticle(ModalTrans.article(article, true)))
                .collect(Collectors.toList());
        pageData.setList(articleModelList);
        return pageData;
    }

    @Override
    public PageData<ArticleModel> findByCategory(String name, int page, int count) {
        Category category = categoryMapper.findCategoryByName(name);
        if (category == null) {
            throw new MyException(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        PageHelper.startPage(page, count);
        List<Article> open = articleMapper.findAllByCategoryIdAndOpen(category.getId());

        List<ArticleModel> modelList = new ArrayList<>();

        modelList = open.stream()
                .map(article -> ModalTrans.article(article, true))
                .peek(articleModel -> {
                    articleModel.setNextArticle(null);
                    articleModel.setPreArticle(null);
                })
                .collect(Collectors.toList());
        return new PageData<>(new PageInfo<>(open), modelList);
    }

    @Override
    public PageData<ArticleModel> findByTag(String name, int page, int count) {
        Tag tag = tagMapper.findTagByName(name);
        if (tag == null) {
            throw new MyException(ResponseEnum.TAG_NOT_EXIST);
        }
        PageHelper.startPage(page, count);
        List<ArticleTag> articleByTag = articleTagMapper.findArticleByTagAndOpen(tag.getId());
        List<ArticleModel> modelList = articleByTag
                .stream()
                .map(articleTag -> ModalTrans.article(articleTag.getArticle(), true))
                .peek(articleModel -> {
                    articleModel.setNextArticle(null);
                    articleModel.setPreArticle(null);
                }).collect(Collectors.toList());
        return new PageData<>(new PageInfo<>(articleByTag), modelList);
    }

    private ArticleModel setPreAndNextArticle(ArticleModel articleModel) {
        if (articleModel == null) {
            return null;
        }
        articleModel.setPreArticle(ModalTrans.article(articleMapper.getPreArticle(articleModel.getId()), true));
        articleModel.setNextArticle(ModalTrans.article(articleMapper.getNextArticle(articleModel.getId()), true));
        return articleModel;
    }
}

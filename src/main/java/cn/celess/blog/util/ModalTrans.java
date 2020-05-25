package cn.celess.blog.util;

import cn.celess.blog.entity.*;
import cn.celess.blog.entity.model.*;
import org.springframework.beans.BeanUtils;

/**
 * @Author: 小海
 * @Date: 2020-05-24 18:04
 * @Desc:
 */
public class ModalTrans {

    public static ArticleModel article(Article article) {
        if (article == null) {
            return null;
        }
        ArticleModel articleModel = new ArticleModel();
        BeanUtils.copyProperties(article, articleModel);
        articleModel.setPublishDateFormat(DateFormatUtil.get(article.getPublishDate()));
        articleModel.setUpdateDateFormat(DateFormatUtil.get(article.getUpdateDate()));
        articleModel.setOriginal(article.getType());
        articleModel.setCategory(article.getCategory().getName());
        articleModel.setAuthor(user(article.getUser()));
        return articleModel;
    }


    public static ArticleModel article(Article article, boolean noMdContent) {
        ArticleModel article1 = article(article);
        if (!noMdContent) {
            return article1;
        }
        article1.setMdContent(null);
        article1.setOpen(null);
        return article1;
    }


    public static UserModel user(User user) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }

    public static CategoryModel category(Category category) {
        CategoryModel model = new CategoryModel();
        BeanUtils.copyProperties(category, model);
        return model;
    }


    public static TagModel tag(Tag tag) {
        TagModel model = new TagModel();
        BeanUtils.copyProperties(tag, model);
        return model;
    }


    public static WebUpdateModel webUpdate(WebUpdate update) {
        WebUpdateModel model = new WebUpdateModel();
        model.setId(update.getId());
        model.setInfo(update.getUpdateInfo());
        model.setTime(DateFormatUtil.get(update.getUpdateTime()));
        return model;
    }
}

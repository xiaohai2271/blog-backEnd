package cn.celess.blog.util;

import cn.celess.blog.enmu.UserAccountStatusEnum;
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
        articleModel.getTags().forEach(tag -> {
            tag.setCategory(null);
            tag.setDeleted(null);
        });
        return articleModel;
    }


    public static ArticleModel article(Article article, boolean noMdContent) {
        ArticleModel article1 = article(article);
        if (!noMdContent || article1 == null) {
            return article1;
        }
        article1.setMdContent(null);
        article1.setOpen(null);
        return article1;
    }

    public static UserModel userFullInfo(User user) {
        if (user == null || user.getId() == -1) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        userModel.setStatus(UserAccountStatusEnum.get(user.getStatus()));
        userModel.setAvatarImgUrl(user.getAvatarImgUrl() == null || user.getAvatarImgUrl().length() == 0 ?
                null :
                "http://cdn.celess.cn/" + user.getAvatarImgUrl());
        userModel.setDisplayName(user.getDisplayName() == null ? user.getEmail() : user.getDisplayName());
        userModel.setRecentlyLandedDate(DateFormatUtil.get(user.getRecentlyLandedDate()));
        return userModel;
    }

    public static UserModel user(User user) {
        UserModel model = userFullInfo(user);
        if (model == null) {
            return null;
        }
        model.setRole(null);
        model.setEmailStatus(null);
        return model;
    }

    public static CategoryModel category(Category category) {
        if (category == null) {
            return null;
        }
        CategoryModel model = new CategoryModel();
        BeanUtils.copyProperties(category, model);
        return model;
    }


    public static TagModel tag(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagModel model = new TagModel();
        BeanUtils.copyProperties(tag, model);
        return model;
    }


    public static WebUpdateModel webUpdate(WebUpdate update) {
        if (update == null) {
            return null;
        }
        WebUpdateModel model = new WebUpdateModel();
        model.setId(update.getId());
        model.setInfo(update.getUpdateInfo());
        model.setTime(DateFormatUtil.get(update.getUpdateTime()));
        return model;
    }

    public static CommentModel comment(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentModel model = new CommentModel();
        BeanUtils.copyProperties(comment, model);
        model.setFromUser(user(comment.getFromUser()));
        model.setToUser(user(comment.getToUser()));
        model.setDate(DateFormatUtil.get(comment.getDate()));
        return model;
    }
}

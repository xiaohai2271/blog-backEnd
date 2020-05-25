package cn.celess.blog.util;

import cn.celess.blog.entity.Article;
import cn.celess.blog.entity.User;
import cn.celess.blog.entity.model.ArticleModel;
import cn.celess.blog.entity.model.UserModel;
import org.springframework.beans.BeanUtils;

/**
 * @Author: 小海
 * @Date: 2020-05-24 18:04
 * @Desc:
 */
public class ModalTrans {

    public static ArticleModel articleToModal(Article article) {
        if (article == null) {
            return null;
        }
        ArticleModel articleModel = new ArticleModel();
        BeanUtils.copyProperties(article, articleModel);
        articleModel.setPublishDateFormat(DateFormatUtil.get(article.getPublishDate()));
        articleModel.setUpdateDateFormat(DateFormatUtil.get(article.getUpdateDate()));
        articleModel.setOriginal(article.getType());
        articleModel.setCategory(article.getCategory().getName());
        articleModel.setAuthor(userToModal(article.getUser()));
        return articleModel;
    }


    public static UserModel userToModal(User user) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }

}

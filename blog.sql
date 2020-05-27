CREATE DATABASE `t_blog`;

USE t_blog;

CREATE TABLE `article`
(
    `a_id`             bigint(20) primary key auto_increment,
    `a_title`          varchar(255) COLLATE utf8mb4_unicode_ci not null unique comment '文章标题',
    `a_summary`        varchar(255) COLLATE utf8mb4_unicode_ci not null comment '文章摘要',
    `a_md_content`     longtext COLLATE utf8mb4_unicode_ci     not null comment '文章Markdown内容',
    `a_url`            tinytext                                         default null comment '转载文章的原文链接',
    `a_author_id`      int                                     not null comment '作者id',
    `a_is_original`    boolean                                          default true comment '文章是否原创',
    `a_reading_number` int                                              default 0 comment '文章阅读数',
    `a_like`           int                                              default 0 comment '文章点赞数',
    `a_dislike`        int                                              default 0 comment '文章不喜欢数',
    `a_category_id`    int                                     not null comment '文章分类id',
    `a_publish_date`   datetime                                         default CURRENT_TIMESTAMP comment '文章发布时间',
    `a_update_date`    datetime                                         default null comment '文章的更新时间',
    `a_is_open`        boolean                                          default true comment '文章是否可见',
    `is_delete`        boolean                                 not null default false comment '该数据是否被删除'
) DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci,comment '文章表';

CREATE TABLE `article_tag`
(
    `at_id`     bigint(20) primary key auto_increment,
    `a_id`      bigint(20) not null comment '文章id',
    `t_id`      bigint     not null comment 'tag/category 的id',
    `is_delete` boolean    not null default false comment '该数据是否被删除'
) comment '文章标签表';

CREATE TABLE `tag_category`
(
    `t_id`        bigint(20) primary key auto_increment,
    `t_name`      varchar(255) not null,
    `is_category` boolean      not null default true,
    `is_delete`   boolean      not null default false comment '该数据是否被删除'
) comment '标签和分类表';

CREATE TABLE `comment`
(
    `co_id`             bigint(20) primary key auto_increment,
    `co_page_path`      varchar(255)                    not null comment '评论/留言的页面',
    `co_content`        text COLLATE utf8mb4_unicode_ci not null comment '评论/留言内容',
    `co_date`           datetime                        not null comment '评论/留言的日期',
    `co_status`         tinyint                         not null default 0 comment '评论的状态',
    `co_pid`            bigint                          not null default -1 comment '评论/留言的父id',
    `co_from_author_id` int                             not null comment '留言者id',
    `co_to_author_id`   int                                      default null comment '父评论的作者id',
    `is_delete`         boolean                         not null default false comment '该数据是否被删除'
) DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci,comment '评论/留言表';

CREATE TABLE `links`
(
    `l_id`        bigint(20) primary key auto_increment,
    `l_name`      varchar(255) COLLATE utf8mb4_unicode_ci not null comment '友站名称',
    `l_is_open`   boolean                                          default true comment '是否公开',
    `l_url`       varchar(255)                            not null comment '首页地址',
    `l_icon_path` varchar(255)                            not null comment '友链的icon地址',
    `l_desc`      varchar(255) COLLATE utf8mb4_unicode_ci not null comment '友链的说明描述',
    `is_delete`   boolean                                 not null default false comment '该数据是否被删除'
) comment '友站表';

CREATE TABLE `visitor`
(
    `v_id`         bigint(20) primary key auto_increment,
    `v_date`       datetime     not null comment '访问时间',
    `v_ip`         varchar(255) not null comment '访客ip',
    `v_user_agent` text comment '访客ua',
    `is_delete`    boolean      not null default false comment '该数据是否被删除'
) comment '访客表';

CREATE TABLE `web_update`
(
    `wu_id`     int primary key auto_increment,
    `wu_info`   varchar(255) COLLATE utf8mb4_unicode_ci not null comment '更新内容',
    `wu_time`   datetime                                not null comment '更新时间',
    `is_delete` boolean                                 not null default false comment '该数据是否被删除'
) comment '更新内容表';

CREATE TABLE `user`
(
    `u_id`                   int         not null primary key auto_increment,
    `u_email`                varchar(50) not null,
    `u_pwd`                  varchar(40) not null comment '密码',
    `u_email_status`         boolean                                default false comment '邮箱验证状态',
    `u_avatar`               varchar(255)                           default null comment '用户头像',
    `u_desc`                 tinytext COLLATE utf8mb4_unicode_ci    default null comment '用户的描述',
    `u_recently_landed_time` datetime                               default null comment '最近的登录时间',
    `u_display_name`         varchar(30) COLLATE utf8mb4_unicode_ci default null comment '展示的昵称',
    `u_role`                 varchar(40) not null                   default 'user' comment '权限组',
    `status`                 tinyint(1)  not null                   default 0 comment '账户状态',
    unique key `uni_user_id` (`u_id`),
    unique key `uni_user_email` (`u_email`)
) comment '用户表';


CREATE VIEW articleView
            (articleId, title, summary, mdContent, url, isOriginal, readingCount, likeCount, dislikeCount,
             publishDate, updateDate, isOpen,
             categoryId, categoryName, tagId, tagName,
             authorId, userEmail, userAvatar, userDisplayName, isDelete)
as
select article.a_id             as articleId,
       article.a_title          as title,
       article.a_summary        as summary,
       article.a_md_content     as mdContent,
       article.a_url            as url,
       article.a_is_original    as isOriginal,
       article.a_reading_number as readingCount,
       article.a_like           as likeCount,
       article.a_dislike        as dislikeCount,
       article.a_publish_date   as publishDate,
       article.a_update_date    as updateDate,
       article.a_is_open        as isOpen,
       category.t_id            as categoryId,
       category.t_name          as categoryName,
       tag.t_id                 as tagId,
       tag.t_name               as tagName,
       user.u_id                as authorId,
       user.u_email             as userEmail,
       user.u_avatar            as userAvatar,
       user.u_display_name      as userDisplayName,
       article.is_delete        as isDelete
from article,
     tag_category as tag,
     tag_category as category,
     article_tag,
     user
where article.a_id = article_tag.a_id
  and article_tag.t_id = tag.t_id
  and tag.is_category = false
  and article.a_category_id = category.t_id
  and category.is_category = true
  and article.a_author_id = user.u_id;


CREATE VIEW commentView
            (commentId, pagePath, content, date, status, pid, toAuthorId, toAuthorEmail, toAuthorDisplayName,
             toAuthorAvatar, fromAuthorId, fromAuthorEmail, fromAuthorDisplayName,
             fromAuthorAvatar, isDelete)
as
select cuT.co_id               as commentId,
       cuT.co_page_path        as pagePath,
       cuT.co_content          as content,
       cuT.co_date             as date,
       cuT.co_status           as status,
       cuT.co_pid              as pid,
       cuT.co_to_author_id     as toAuthorId,
       cuT.toEmail             as toAuthorEmail,
       cuT.toDisplayName       as toAuthorDisplayName,
       cuT.toAvatar            as toAuthorAvatar,
       userFrom.u_id           as fromAuthorId,
       userFrom.u_email        as fromAuthorEmail,
       userFrom.u_display_name as fromAuthorDisplayName,
       userFrom.u_avatar       as fromAuthorAvatar,
       cuT.is_delete           as isDelete
from (select comment.co_id,
             comment.co_page_path,
             comment.co_content,
             comment.co_date,
             comment.co_status,
             comment.co_pid,
             comment.co_from_author_id,
             comment.co_to_author_id,
             userTo.u_email        as toEmail,
             userTo.u_display_name as toDisplayName,
             userTo.u_avatar       as toAvatar,
             comment.is_delete
      from comment
               left join user userTo on (comment.co_to_author_id = userTo.u_id)
     ) as cuT,
     user as userFrom
where cuT.co_from_author_id = userFrom.u_id;


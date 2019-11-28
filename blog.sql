CREATE DATABASE `blog`;

USE blog;

CREATE TABLE `article`
(
    `a_id`             bigint(20) primary key auto_increment,
    `a_title`          varchar(255) not null unique comment '文章标题',
    `a_summary`        varchar(255) not null comment '文章摘要',
    `a_md_content`     longtext     not null comment '文章Markdown内容',
    `a_tags_id`        varchar(255) not null comment '标签id \',\'处于最尾端',
    `a_category_id`    bigint(20)   not null comment '分类的id',
    `a_url`            tinytext   default null comment '转载文章的原文链接',
    `a_author_id`      bigint(20)   not null comment '作者id',
    `a_is_open`        boolean    default true comment '文章是否可见',
    `a_is_original`    boolean    default true comment '文章是否原创',
    `next_a_id`        bigint(20) default -1 comment '下篇文章id',
    `pre_a_id`         bigint(20) default -1 comment '前一篇文章的id',
    `a_reading_number` int        default 0 comment '文章阅读数',
    `a_publish_date`   datetime     not null comment '文章发布时间',
    `a_update_date`    datetime   default null comment '文章的更新时间'
) DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci,comment '文章表';

CREATE TABLE `tag`
(
    `tag_id`   bigint(20) primary key auto_increment,
    `tag_name` varchar(255) unique not null,
    `articles` tinytext default null comment 'tag对应的文章id'
) comment '标签表';

CREATE table `category`
(
    `c_id`     bigint(20) primary key auto_increment,
    `c_name`   varchar(255) unique not null,
    `articles` varchar(255) comment '分类下的文章'
)comment '分类表';

CREATE TABLE `comment`
(
    `co_id`          bigint(20) primary key auto_increment,
    `co_article_id`  bigint(20)          default -1 comment '文章id',
    `is_comment`     boolean             default true comment '是否是评论',
    `author_id`      bigint(20) not null comment '留言者id',
    `co_content`     text       not null comment '评论/留言内容',
    `co_date`        datetime   not null comment '评论/留言的日期',
    `co_pid`         bigint     not null default -1 comment '评论/留言的父id',
    `co_response_id` tinytext
) DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci,comment '评论/留言表';

CREATE TABLE `links`
(
    `site_id`   bigint(20) primary key auto_increment,
    `site_name` varchar(255) not null comment '友站名称',
    `is_open`   boolean default true comment '是否公开',
    `site_url`  varchar(255) not null comment '首页地址'
) comment '友站表';

CREATE TABLE `visitor`
(
    `v_id`         bigint(20) primary key auto_increment,
    `v_date`       datetime     not null comment '访问时间',
    `v_ip`         varchar(255) not null comment '访客ip',
    `v_user_agent` text comment '访客ua'
) comment '访客表';



CREATE TABLE IF NOT EXISTS `web_update`
(
    `update_id`   bigint(20) primary key auto_increment,
    `update_info` varchar(255) not null comment '更新内容',
    `update_time` datetime     not null comment '更新时间'
) comment '更新内容表';

create table `user`
(
    `u_id`                 int         not null primary key auto_increment,
    `u_email`              varchar(50) not null,
    `u_uid`                varchar(40)          default null comment '用户唯一标识码',
    `u_pwd`                varchar(40) not null comment '密码',
    `email_status`         boolean              default false comment '邮箱验证状态',
    `u_avatar`             varchar(255) comment '用户头像',
    `u_desc`               tinytext comment '用户的描述',
    `recently_landed_time` datetime comment '最近的登录时间',
    `email_verify_id`      varchar(40) comment '用于找回密码或验证邮箱的id',
    `display_name`         varchar(30) comment '展示的昵称',
    `role`                 varchar(40) not null default 'user' comment '权限组',
    unique key `uni_user_id` (`u_id`),
    unique key `uni_user_uid` (`u_uid`),
    unique key `uni_user_email` (`u_email`)
) comment '用户表';


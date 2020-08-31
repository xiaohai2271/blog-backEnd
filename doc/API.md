 > 拷贝的swagger自动生成的api文档，部分字段描述不够准确，详情参考具体代码实现

# **小海博客的APi**


**简介**：<p>小海博客的APi</p>


**HOST**:127.0.0.1:8081

**联系人**:小海

**Version**:1.0

**接口路径**：/v2/api-docs


# article-controller

## create


**接口描述**:


**接口地址**:`/admin/article/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"category": "",
	"id": 0,
	"mdContent": "",
	"open": true,
	"tags": [],
	"title": "",
	"type": true,
	"url": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|body| body  | body | true |ArticleReq  | ArticleReq   |

**schema属性说明**



**ArticleReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|category|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|mdContent|   | body | false |string  |    |
|open|   | body | false |boolean  |    |
|tags|   | body | false |array  |    |
|title|   | body | false |string  |    |
|type|   | body | false |boolean  |    |
|url|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## delete


**接口描述**:


**接口地址**:`/admin/article/del`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|articleID| articleID  | query | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## update


**接口描述**:


**接口地址**:`/admin/article/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"category": "",
	"id": 0,
	"mdContent": "",
	"open": true,
	"tags": [],
	"title": "",
	"type": true,
	"url": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|body| body  | body | true |ArticleReq  | ArticleReq   |

**schema属性说明**



**ArticleReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|category|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|mdContent|   | body | false |string  |    |
|open|   | body | false |boolean  |    |
|tags|   | body | false |array  |    |
|title|   | body | false |string  |    |
|type|   | body | false |boolean  |    |
|url|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## adminArticles


**接口描述**:


**接口地址**:`/admin/articles`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|deleted| deleted  | query | false |boolean  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## retrieveOneById


**接口描述**:


**接口地址**:`/article/articleID/{articleID}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|articleID| articleID  | path | true |integer  |    |
|update| update  | query | false |boolean  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## articles


**接口描述**:


**接口地址**:`/articles`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## findByCategory


**接口描述**:


**接口地址**:`/articles/category/{name}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|name| name  | path | true |string  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## findByTag


**接口描述**:


**接口地址**:`/articles/tag/{name}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|name| name  | path | true |string  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## createSitemap


**接口描述**:


**接口地址**:`/createSitemap`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# category-controller

## addOne


**接口描述**:


**接口地址**:`/admin/category/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|name| name  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## deleteOne


**接口描述**:


**接口地址**:`/admin/category/del`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## updateOne


**接口描述**:


**接口地址**:`/admin/category/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |
|name| name  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getPage


**接口描述**:


**接口地址**:`/categories`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# comment-controller

## adminComment


**接口描述**:


**接口地址**:`/admin/comment/pagePath/{pagePath}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |
|pagePath| pagePath  | path | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## retrievePage


**接口描述**:


**接口地址**:`/comment/pagePath/{pagePath}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |
|pagePath| pagePath  | path | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## commentsOfArticle


**接口描述**:


**接口地址**:`/comments/{pagePath}/{pid}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |
|pagePath| pagePath  | path | true |string  |    |
|pid| pid  | path | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## addOne


**接口描述**:


**接口地址**:`/user/comment/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"content": "",
	"id": 0,
	"pagePath": "",
	"pid": 0,
	"toUserId": 0
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|reqBody| reqBody  | body | true |CommentReq  | CommentReq   |

**schema属性说明**



**CommentReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|content|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|pagePath|   | body | false |string  |    |
|pid|   | body | false |integer(int64)  |    |
|toUserId|   | body | false |integer(int64)  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## delete


**接口描述**:


**接口地址**:`/user/comment/del`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## userComment


**接口描述**:


**接口地址**:`/user/comment/pagePath/{pagePath}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |
|pagePath| pagePath  | path | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## update


**接口描述**:


**接口地址**:`/user/comment/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"content": "",
	"id": 0,
	"pagePath": "",
	"pid": 0,
	"toUserId": 0
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|reqBody| reqBody  | body | true |CommentReq  | CommentReq   |

**schema属性说明**



**CommentReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|content|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|pagePath|   | body | false |string  |    |
|pid|   | body | false |integer(int64)  |    |
|toUserId|   | body | false |integer(int64)  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# common-controller

## bingPic


**接口描述**:


**接口地址**:`/bingPic`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## allCount


**接口描述**:


**接口地址**:`/counts`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## headerInfo


**接口描述**:


**接口地址**:`/headerInfo`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getImg


**接口描述**:


**接口地址**:`/imgCode`


**请求方式**：`GET`


**consumes**:``


**produces**:`["image/png"]`



**请求参数**：
暂无



**响应示例**:

```json

```

**响应参数**:


暂无





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## upload


**接口描述**:


**接口地址**:`/imgUpload`


**请求方式**：`POST`


**consumes**:`["multipart/form-data"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|editormd-image-file| editormd-image-file  | formData | true |file  |    |

**响应示例**:

```json

```

**响应参数**:


暂无





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  ||
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## verCode


**接口描述**:


**接口地址**:`/verCode`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|code| code  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# links-controller

## all


**接口描述**:


**接口地址**:`/admin/links`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | true |integer  |    |
|page| page  | query | true |integer  |    |
|deleted| deleted  | query | false |boolean  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## create


**接口描述**:


**接口地址**:`/admin/links/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"desc": "",
	"iconPath": "",
	"id": 0,
	"name": "",
	"open": true,
	"url": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|reqBody| reqBody  | body | true |LinkReq  | LinkReq   |

**schema属性说明**



**LinkReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|desc|   | body | false |string  |    |
|iconPath|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|name|   | body | false |string  |    |
|open|   | body | false |boolean  |    |
|url|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## del


**接口描述**:


**接口地址**:`/admin/links/del/{id}`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | path | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## update


**接口描述**:


**接口地址**:`/admin/links/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"desc": "",
	"iconPath": "",
	"id": 0,
	"name": "",
	"open": true,
	"url": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|reqBody| reqBody  | body | true |LinkReq  | LinkReq   |

**schema属性说明**



**LinkReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|desc|   | body | false |string  |    |
|iconPath|   | body | false |string  |    |
|id|   | body | false |integer(int64)  |    |
|name|   | body | false |string  |    |
|open|   | body | false |boolean  |    |
|url|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## apply


**接口描述**:


**接口地址**:`/apply`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"desc": "",
	"email": "",
	"iconPath": "",
	"linkUrl": "",
	"name": "",
	"url": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|linkApplyReq| linkApplyReq  | body | true |LinkApplyReq  | LinkApplyReq   |

**schema属性说明**



**LinkApplyReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|desc|   | body | false |string  |    |
|email|   | body | false |string  |    |
|iconPath|   | body | false |string  |    |
|linkUrl|   | body | false |string  |    |
|name|   | body | false |string  |    |
|url|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## allForOpen


**接口描述**:


**接口地址**:`/links`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## reapply


**接口描述**:


**接口地址**:`/reapply`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|key| key  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# tag-controller

## addOne


**接口描述**:


**接口地址**:`/admin/tag/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|name| name  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## delOne


**接口描述**:


**接口地址**:`/admin/tag/del`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## updateOne


**接口描述**:


**接口地址**:`/admin/tag/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |
|name| name  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getPage


**接口描述**:


**接口地址**:`/tags`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getTagNameAndCount


**接口描述**:


**接口地址**:`/tags/nac`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# user-controller

## updateInfoByAdmin


**接口描述**:


**接口地址**:`/admin/user`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"desc": "",
	"displayName": "",
	"email": "",
	"emailStatus": true,
	"id": 0,
	"pwd": "",
	"role": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|user| user  | body | true |UserReq  | UserReq   |

**schema属性说明**



**UserReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|desc|   | body | false |string  |    |
|displayName|   | body | false |string  |    |
|email|   | body | false |string  |    |
|emailStatus|   | body | false |boolean  |    |
|id|   | body | false |integer(int64)  |    |
|pwd|   | body | false |string  |    |
|role|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## multipleDelete


**接口描述**:


**接口地址**:`/admin/user/delete`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`


**请求示例**：
```json
[
	0
]
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|ids| ids  | body | true |array  | integer   |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## delete


**接口描述**:


**接口地址**:`/admin/user/delete/{id}`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | path | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## getAllUser


**接口描述**:


**接口地址**:`/admin/users`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | true |integer  |    |
|page| page  | query | true |integer  |    |
|status| status  | query | false |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getEmailStatus


**接口描述**:


**接口地址**:`/emailStatus/{email}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | path | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## emailVerify


**接口描述**:


**接口地址**:`/emailVerify`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | query | true |string  |    |
|verifyId| verifyId  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## login


**接口描述**:


**接口地址**:`/login`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`


**请求示例**：
```json
{
	"email": "",
	"isRememberMe": true,
	"password": ""
}
```


**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|loginReq| loginReq  | body | true |LoginReq  | LoginReq   |

**schema属性说明**



**LoginReq**

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email|   | body | false |string  |    |
|isRememberMe|   | body | false |boolean  |    |
|password|   | body | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## logout


**接口描述**:


**接口地址**:`/logout`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## registration


**接口描述**:


**接口地址**:`/registration`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | query | true |string  |    |
|password| password  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## resetPwd


**接口描述**:


**接口地址**:`/resetPwd`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | query | true |string  |    |
|pwd| pwd  | query | true |string  |    |
|verifyId| verifyId  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## sendResetPwdEmail


**接口描述**:


**接口地址**:`/sendResetPwdEmail`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## sendVerifyEmail


**接口描述**:


**接口地址**:`/sendVerifyEmail`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|email| email  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## upload


**接口描述**:


**接口地址**:`/user/imgUpload`


**请求方式**：`POST`


**consumes**:`["multipart/form-data"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|file| file  | formData | true |file  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## setPwd


**接口描述**:


**接口地址**:`/user/setPwd`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|confirmPwd| confirmPwd  | query | true |string  |    |
|newPwd| newPwd  | query | true |string  |    |
|pwd| pwd  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getUserInfo


**接口描述**:


**接口地址**:`/user/userInfo`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## updateInfo


**接口描述**:


**接口地址**:`/user/userInfo/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|desc| desc  | query | false |string  |    |
|displayName| displayName  | query | false |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# visitor-controller

## page


**接口描述**:


**接口地址**:`/admin/visitor/page`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | false |integer  |    |
|page| page  | query | false |integer  |    |
|showLocation| showLocation  | query | false |boolean  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## dayVisitCount


**接口描述**:


**接口地址**:`/dayVisitCount`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getIp


**接口描述**:


**接口地址**:`/ip`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## ipLocation


**接口描述**:


**接口地址**:`/ip/{ip}`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|ip| ip  | path | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## add


**接口描述**:


**接口地址**:`/visit`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## getVisitorCount


**接口描述**:


**接口地址**:`/visitor/count`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
# web-update-info-controller

## create


**接口描述**:


**接口地址**:`/admin/webUpdate/create`


**请求方式**：`POST`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|info| info  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## del


**接口描述**:


**接口地址**:`/admin/webUpdate/del/{id}`


**请求方式**：`DELETE`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | path | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 204 | No Content  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
## update


**接口描述**:


**接口地址**:`/admin/webUpdate/update`


**请求方式**：`PUT`


**consumes**:`["application/json"]`


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|id| id  | query | true |integer  |    |
|info| info  | query | true |string  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 201 | Created  ||
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## lastestUpdateTime


**接口描述**:


**接口地址**:`/lastestUpdate`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## findAll


**接口描述**:


**接口地址**:`/webUpdate`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：
暂无



**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||
## page


**接口描述**:


**接口地址**:`/webUpdate/pages`


**请求方式**：`GET`


**consumes**:``


**produces**:`["*/*"]`



**请求参数**：

| 参数名称         | 参数说明     |     in |  是否必须      |  数据类型  |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
|count| count  | query | true |integer  |    |
|page| page  | query | true |integer  |    |

**响应示例**:

```json
{
	"code": 0,
	"msg": "",
	"result": {}
}
```

**响应参数**:


| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
|code|   |integer(int32)  | integer(int32)   |
|msg|   |string  |    |
|result|   |object  |    |





**响应状态**:


| 状态码         | 说明                            |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |Response|
| 401 | Unauthorized  ||
| 403 | Forbidden  ||
| 404 | Not Found  ||

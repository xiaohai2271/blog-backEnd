# 如何构建
### 1. 构建前准备
- 安装jdk
- 安装maven(也可使使用项目中的maven wrapper)
- 安装mysql
- 安装redis
- 获取七牛云的AccessKey/SecretKey (个人中心-密钥管理) [七牛云官网](https://www.qiniu.com/)

### 2. 拉取项目到本地
``` shell script
git clone https://github.com/xiaohai2271/blog-backEnd.git
# 或
git clone git@github.com:xiaohai2271/blog-backEnd.git
```
 
### 3. maven构建
```shell script
mvn clean package -pl blog-deploy -am
```

### 4. 运行
```shell script
java -jar blog-deploy/target/blog-*.jar
```

### 5. 其他命令
```shell script
mvn clean # 清理项目资源
mvn clean package # 清理项目资源并重新打包
mvn clean package -DskipTests # 清理项目资源,重新打包并跳过测试
mvn test # 运行测试 
..... #待添加
```
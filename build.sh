#!/bin/sh
echo "查询已存在进程"
pgrep -af blog-0.0.1-SNAPSHOT.jar
echo "结束已存在进程"
pkill -f blog-0.0.1-SNAPSHOT.jar
echo "开始运行小海博客"
nohup java -jar -Dfile.encoding=UTF-8 blog-0.0.1-SNAPSHOT.jar >blog.log &
echo "更新结束"

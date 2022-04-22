#!/usr/bin/env bash
# 定义应用组名
pwd

cd /mnt/jenkinsfile-demo

group_name=''
# 定义应用名称
app_name='jenkinsfile-demo'
# 定义应用版本
app_version='v1'
docker stop ${app_name}
echo '----stop container----'
docker rm ${app_name}
echo '----rm container----'
#docker rmi ${group_name}/${app_name}:${app_version}
docker rmi ${app_name}:${app_version}
echo '----rm image----'
# 打包编译docker镜像
#docker build -t ${group_name}/${app_name}:${app_version} .
docker build -t ${app_name}:${app_version} .
echo '----build image----'
docker run -it -p 9191:8080 \
  -v /etc/localtime:/etc/localtime \
  --restart=on-failure:10 \
  -e "JAVA_OPTS=-Xms256m -Xmx256m" \
  --name ${app_name} \
  -d ${app_name}:${app_version}
#-d ${group_name}/${app_name}:${app_version}
echo '----start container----'
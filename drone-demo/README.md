# drone插件
http://plugins.drone.io/
# 参考文章
https://xie.infoq.cn/article/b1e1aa33c5d2e7841592b2786

https://www.weiye.link/197.html

# gitea官网

https://gitea.io/zh-cn/

# Harbor官网

https://goharbor.io/

192.168.1.162服务用户名密码

admin/Harbor12345

## Harbor安装

运行
```shell
sudo ./install.sh
```
会生成`docker-compose.yml`文件

# 安装命令

可以使用`docker-compose`安装
```shell script
# 安装gitea
docker run -d --name=gitea -p 10022:22 -p 3000:3000 -v /mnt/gitea:/data gitea/gitea:latest

# 安装drone
docker run \
  --volume=/mnt/drone:/data \
  --env=DRONE_GITEA_SERVER=http://192.168.1.160:3000 \
  --env=DRONE_GITEA_CLIENT_ID=59ae7d77-31c5-42bc-8ff2-16ea949f2705 \
  --env=DRONE_GITEA_CLIENT_SECRET=NIKkKVCBgJS5UiN5erAd2zNOFGh1lzkFH4ziIOwrHxQ= \
  --env=DRONE_RPC_SECRET=ff0c3bbb1770561e9feca3cb61e8269a \
  --env=DRONE_SERVER_HOST=192.168.1.161 \
  --env=DRONE_SERVER_PROTO=http \
  --env=DRONE_USER_CREATE=username:powertime,machine:false,admin:true \
  --publish=80:80 \
  --publish=443:443 \
  --restart=always \
  --detach=true \
  --name=drone \
  drone/drone:1
  
# 安装runner 
docker run -d \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -e DRONE_RPC_PROTO=http \
  -e DRONE_RPC_HOST=192.168.1.161 \
  -e DRONE_RPC_SECRET=ff0c3bbb1770561e9feca3cb61e8269a \
  -e DRONE_RUNNER_CAPACITY=2 \
  -e DRONE_RUNNER_NAME=${HOSTNAME} \
  -p 3000:3000 \
  --restart always \
  --name runner \
  drone/drone-runner-docker:1
```
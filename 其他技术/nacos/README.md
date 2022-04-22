Nacos版本0.3.0

docker启动查看https://hub.docker.com/r/paderlol/nacos/
简单启动 sudo docker run --env MODE=standalone --name nacos-quick-start -d  -p 8848:8848  nacos:0.3.0

内容：

快速启动
docker run --env MODE=standalone --name nacos-quick-start -d  -p 8848:8848  paderlol/nacos:0.2.1-RC

查看启动日志
## 注意日志只是针对start.log 其他日志如果需要查看可以自定义挂卷
docker logs -f 容器名/或者ID


docker -compose启动
version: 0.2.1-RC
services:
  nacos:
    image: paderlol/nacos:0.2.1-RC
    container_name: nacos-quick-start
    environment:
    - MODE=standalone
    ports:
    - "8848:8848"
    
## 可自定义变量

变量名	                    默认值	    描述
SERVER_SERVLET_CONTEXTPATH	/nacos	    上下文路径
MODE	                    cluster	    启动模式：standalone/cluster
SERVER_PORT	                8848	    容器启动服务的端口


## 自定义卷
描述	            容器路径
日志路径	        /home/nacos/logs/
配置路径	        /home/nacos/conf/
jar路径	        /home/nacos/target/nacos-server.jar


## 下面这个是自定义日志路径的例子

version: 0.2.1-RC
services:
  nacos:
    image: paderlol/nacos:0.2.1
    container_name: nacos-standalone
    environment:
    - MODE: standalone
    volumes:
      - 宿主机:/home/nacos/logs
    ports:
    - "8848:8848"
docker-compose up -d && docker exec -t nacos1 cat conf/cluster.conf



## 集群配置
version: "2"
services:
  nacos1:
    image: paderlol/nacos:0.1.0
    container_name: nacos1
    networks:
      nacos_net:
        ipv4_address: 172.16.238.10
    volumes:
    - /宿主机目录:/home/nacos/logs
    ports:
    - "8848:8848"
    - "9555:9555"
    environment:
    - NACOS_SERVERS=172.16.238.10:8848 172.16.238.11:8848 172.16.238.12:8848
    - DB_HOST_ZERO=172.16.238.13
    - DB_NAME_ZERO=nacos_devtest
    - DB_PORT_ZERO=3306
    - DB_HOST_ONE=172.16.238.14
    - DB_NAME_ONE=nacos_devtest
    - DB_PORT_ONE=3306
    - DB_USER=nacos
    - DB_PASSWORD=nacos
    restart: always
    depends_on:
    - mysql1
    - mysql2

  nacos2:
    image: paderlol/nacos:0.1.0
    container_name: nacos2
    networks:
      nacos_net:
        ipv4_address: 172.16.238.11
    ports:
    - "8849:8848"
    environment:
    - NACOS_SERVERS=172.16.238.10:8848 172.16.238.11:8848 172.16.238.12:8848
    - DB_HOST_ZERO=172.16.238.13
    - DB_NAME_ZERO=nacos_devtest
    - DB_PORT_ZERO=3306
    - DB_HOST_ONE=172.16.238.14
    - DB_NAME_ONE=nacos_devtest
    - DB_PORT_ONE=3306
    - DB_USER=nacos
    - DB_PASSWORD=nacos
    restart: always
    depends_on:
    - mysql1
    - mysql2
  nacos3:
    image: paderlol/nacos:0.1.0
    container_name: nacos3
    networks:
      nacos_net:
        ipv4_address: 172.16.238.12
    volumes:
    - /宿主机目录:/var/lib/mysql
    ports:
    - "8850:8848"
    environment:
    - NACOS_SERVERS=172.16.238.10:8848 172.16.238.11:8848 172.16.238.12:8848
    - DB_HOST_ZERO=172.16.238.13
    - DB_NAME_ZERO=nacos_devtest
    - DB_PORT_ZERO=3306
    - DB_HOST_ONE=172.16.238.14
    - DB_NAME_ONE=nacos_devtest
    - DB_PORT_ONE=3306
    - DB_USER=nacos
    - DB_PASSWORD=nacos
    restart: always
    depends_on:
    - mysql1
    - mysql2
  mysql1:
    image: mysql:5.7
    networks:
      nacos_net:
        ipv4_address: 172.16.238.13
    environment:
    - MYSQL_ROOT_PASSWORD=root
    - MYSQL_DATABASE=nacos_devtest
    - MYSQL_USER=nacos
    - MYSQL_PASSWORD=nacos
    volumes:
    - /宿主机目录/mysql:/var/lib/mysql
    ports:
    - "3306:3306"
  mysql2:
    image: mysql:5.7
    networks:
      nacos_net:
        ipv4_address: 172.16.238.14
    environment:
    - MYSQL_ROOT_PASSWORD=root
    - MYSQL_DATABASE=nacos_devtest
    - MYSQL_USER=nacos
    - MYSQL_PASSWORD=nacos
    ports:
    - "3305:3306"
networks:
  nacos_net:
    driver: bridge
    ipam:
      driver: default
      config:
      - subnet: 172.16.238.0/24
Note: 注意第一次启动需要去数据库初始化SQL脚本 然后重启

docker-compose up -d && docker exec -t nacos1 cat conf/cluster.conf
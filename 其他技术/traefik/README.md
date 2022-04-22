## 简介
[traefik](https://traefik.io/) 是一款开源的反向代理与负载均衡工具。它最大的优点是能够与常见的微服务系统直接整合，可以实现自动化动态配置。目前支持 Docker, Swarm, Mesos/Marathon, Mesos, Kubernetes, Consul, Etcd, Zookeeper, BoltDB, Rest API 等等后端模型。

**为什么选择 traefik？**

* Golang 编写，单文件部署，与系统无关，同时也提供小尺寸 Docker 镜像。
* 支持 Docker/Etcd 后端，天然连接我们的微服务集群。
* 内置 Web UI，管理相对方便。
* 自动配置 ACME(Let’s Encrypt) 证书功能。
* 性能尚可，我们也没有到压榨 LB 性能的阶段，易用性更重要。
* Restful API 支持。
* 支持后端健康状态检查，根据状态自动配置。
* 支持动态加载配置文件和 graceful 重启。
* 支持 WebSocket 和 HTTP/2。

**缺点**
traefik不能作为文件服务器使用（_学习完没有发现_）

## Docker启动Traefik
```jshelllanguage
sudo docker run -d -p 8080:8080 -p 80:80 -v /var/run/docker.sock:/var/run/docker.sock -v /vagrant/traefik.toml:/etc/traefik/traefik.toml traefik
```

## traefik配置
 traefik.toml文件 它为最简单的配置没有任何负载均衡
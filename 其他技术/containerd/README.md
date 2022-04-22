# 1. `Containerd`简介
很早之前的 Docker Engine 中就有了containerd，只不过现在是将 containerd 从 Docker Engine 里分离出来，作为一个独立的开源项目，目标是提供一个更加开放、稳定的容器运行基础设施。分离出来的 containerd 将具有更多的功能，涵盖整个容器运行时管理的所有需求，提供更强大的支持。

简单的来说，containerd 是一个工业级标准的容器运行时，它强调简单性、健壮性和可移植性。containerd可以在宿主机中管理完整的容器生命周期，包括容器镜像的传输和存储、容器的执行和管理、存储和网络等。

## 1.1. `Containerd`安装
```shell
wget https://github.com/containerd/containerd/releases/download/v1.5.7/containerd-1.5.7-linux-amd64.tar.gz
tar zxf containerd-1.5.7-linux-amd64.tar.gz -C /usr/local/
```

## 1.2. 生成默认配置文件
```shell
containerd config default > /etc/containerd/config.toml
```

## 1.3. 配置`Containerd`作为服务运行
```shell
vi /lib/systemd/system/containerd.service
[Unit]
Description=containerd container runtime
Documentation=https://containerd.io
After=network.target

[Service]
ExecStartPre=/sbin/modprobe overlay
ExecStart=/usr/local/bin/containerd
Delegate=yes
KillMode=process
LimitNOFILE=1048576
# Having non-zero Limit*s causes performance problems due to accounting overhead
# in the kernel. We recommend using cgroups to do container-local accounting.
LimitNPROC=infinity
LimitCORE=infinity

[Install]
WantedBy=multi-user.target
```

## 1.4. 启动服务
```shell
systemctl daemon-reload
systemctl start containerd.service
systemctl status containerd.service
```

# 2. 安装`runc`
```shell
wget https://github.com/opencontainers/runc/releases/download/v1.0.2/runc.amd64
# 执行Path和权限设置
mv runc.amd64 /usr/local/sbin/runc 
chmod +x /usr/local/sbin/runc
```

# 3. 客户端`crictl`
```shell
VERSION="v1.22.0"
wget https://github.com/kubernetes-sigs/cri-tools/releases/download/$VERSION/crictl-$VERSION-linux-amd64.tar.gz
sudo tar zxvf crictl-$VERSION-linux-amd64.tar.gz -C /usr/local/bin
```
## 3.1. 配置`crictl`
```shell
vi /etc/crictl.yaml

runtime-endpoint: unix:///run/containerd/containerd.sock
image-endpoint: unix:///run/containerd/containerd.sock
timeout: 2
debug: true
pull-image-on-create: false
```
[crictl配置详解](https://github.com/kubernetes-sigs/cri-tools/blob/master/docs/crictl.md)

## 3.2. `Docker`和`Containerd`两种容器引擎常用命令对比
Docker运行时和安全沙箱运行时的容器引擎分别是Docker和Containerd。这两种容器引擎都有各自的命令工具来管理镜像和容器。两种容器引擎常用命令对比如下。
<table>
<tr>
<th rowspan="2" style="text-align:center;">命令</th>
<th rowspan="2" style="text-align:center;">Docker docker</th>
<th colspan="2" style="text-align:center;">Containerd</th>
</tr>
<tr>
<th style="text-align:center;">crictl（推荐）</th>
<th style="text-align:center;">ctr</th>
</tr>
<tr>
<td>查看容器列表</td>
<td>docker ps</td>
<td>crictl ps</td>
<td>ctr -n k8s.io c ls</td>
</tr>
<tr>
<td>查看容器详情</td>
<td>docker inspect</td>
<td>crictl inspect</td>
<td>ctr -n k8s.io c info</td>
</tr>
<tr>
<td>查看容器日志</td>
<td>docker logs</td>
<td>crictl logs</td>
<td>无</td>
</tr>
<tr>
<td>容器内执行命令</td>
<td>docker exec</td>
<td>crictl exec</td>
<td>无</td>
</tr>
<tr>
<td>挂载容器</td>
<td>docker attach</td>
<td>crictl attach</td>
<td>无</td>
</tr>
<tr>
<td>显示容器资源使用情况</td>
<td>docker stats</td>
<td>crictl stats</td>
<td>无</td>
</tr>
<tr>
<td>创建容器</td>
<td>docker create</td>
<td>crictl create</td>
<td>ctr -n k8s.io c create</td>
</tr>
<tr>
<td>启动容器</td>
<td>docker start</td>
<td>crictl start</td>
<td>ctr -n k8s.io run</td>
</tr>
<tr>
<td>停止容器</td>
<td>docker stop</td>
<td>crictl stop</td>
<td>无</td>
</tr>
<tr>
<td>删除容器</td>
<td>docker rm</td>
<td>crictl rm</td>
<td>ctr -n k8s.io c del</td>
</tr>
<tr>
<td>查看镜像列表</td>
<td>docker images</td>
<td>crictl images</td>
<td>ctr -n k8s.io i ls</td>
</tr>
<tr>
<td>查看镜像详情</td>
<td>docker inspect</td>
<td>crictl inspecti</td>
<td>无</td>
</tr>
<tr>
<td>拉取镜像</td>
<td>docker pull</td>
<td>crictl pull</td>
<td>ctr -n k8s.io i pull</td>
</tr>
<tr>
<td>推送镜像</td>
<td>docker push</td>
<td>无</td>
<td>ctr -n k8s.io i push</td>
</tr>
<tr>
<td>删除镜像</td>
<td>docker rmi</td>
<td>crictl rmi</td>
<td>ctr -n k8s.io i rm</td>
</tr>
<tr>
<td>查看Pod列表</td>
<td>无</td>
<td>crictl pods</td>
<td>无</td>
</tr>
<tr>
<td>查看Pod详情</td>
<td>无</td>
<td>crictl inspectp</td>
<td>无</td>
</tr>
<tr>
<td>启动Pod</td>
<td>无</td>
<td>crictl runp</td>
<td>无</td>
</tr>
<tr>
<td>停止Pod</td>
<td>无</td>
<td>crictl stopp</td>
<td>无</td>
</tr>
</table>

## 4. 参考网站
[Containerd容器运行时初识与尝试](https://www.modb.pro/db/88391)

[一文搞懂容器运行时 Containerd](https://www.qikqiak.com/post/containerd-usage/)

[Containerd 入门实战](https://segmentfault.com/a/1190000040035380)

[Docker 被K8S抛弃！别慌！分分钟转型 Containerd](https://segmentfault.com/a/1190000040178226)

[CentOS7下 kubernetes containerd版安装](https://blog.csdn.net/flywingwu/article/details/113482681)

[Containerd 二进制安装与使用](https://www.jianshu.com/p/a70d2d12664b)
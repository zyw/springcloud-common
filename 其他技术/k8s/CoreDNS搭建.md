# 1. 什么是CoreDNS？
CoreDNS是DNS服务器。它用Go编写。

CoreDNS与其他DNS服务器不同，例如（都非常出色） [Bind](https://www.isc.org/blogs/category/bind/)， [Knot](https://www.knot-dns.cz/)， [PowerDNS](https://www.powerdns.com/)和 [Unbound](https://www.unbound.net/)（从技术上讲是解析器，但仍然值得一提），因为它非常灵活，几乎所有功能都外包给了插件。

插件可以是独立的，也可以一起执行“ DNS功能”。

那么什么是“ DNS功能”？出于CoreDNS的目的，我们将其定义为实现CoreDNS Plugin API的软件。实现的功能可能会大相径庭。有些插件本身并不会创建响应，例如[指标](https://coredns.io/plugins/metrics)或 [缓存](https://coredns.io/plugins/cache)，但是会添加功能。然后有一些插件*确实会*产生响应。这些也可以做任何事情：有一些与[Kubernetes](https://coredns.io/plugins/kubernetes)通信 以提供服务发现的插件，一些从[文件](https://coredns.io/plugins/file)或[数据库](https://coredns.io/explugins/pdsql)读取数据的插件。

目前，默认的CoreDNS安装中包含大约30个插件，但是您也可以将一大堆[外部](https://coredns.io/explugins)插件编译为CoreDNS以扩展其功能。

编写新[插件](https://coredns.io/manual/toc/#writing-plugins)应该很容易，但是需要了解Go并对DNS的工作方式有一定的了解。CoreDNS提取了许多DNS详细信息，因此您可以专注于编写所需的插件功能。

# 2. CoreDNS安装

CoreDNS是用Go编写的，但是除非您想自己开发插件或编译CoreDNS，否则您可能不需要关心。以下各节详细介绍如何获取CoreDNS二进制文件或从源代码安装。

## 2.1. 二进制文件

对于每个CoreDNS版本，我们都为各种操作系统提供[预编译的二进制文件](https://github.com/coredns/coredns/releases/latest)。对于Linux，我们还为ARM，PowerPC和其他体系结构提供了交叉编译的二进制文件。

### 2.1.1. 下载

```shell
# 下载安装文件
wget https://github.com/coredns/coredns/releases/download/v1.7.0/coredns_1.7.0_darwin_amd64.tgz
# 解压到指定目录
tar zxf coredns_1.5.0_linux_amd64.tgz -C /usr/local/bin/
```

### 2.1.2. 配置

在`/etc/coredns/`目录，创建`Corefile`文件，并编辑。因为我们的网络环境比较简单所以使用`hosts`配置方式配置。

```nginx
.xa:53 {
    hosts /etc/coredns/hosts
    log
}

.:53 {
    forward . 223.5.5.5:53 223.6.6.6 1.1.1.1
    log
}
```

在`/etc/coredns/`下创建`hosts`文件配置如下：

```
192.168.1.150   vm.xa
192.168.1.151   racher.xa
192.168.1.152   test.xa
192.168.1.154   dev.xa
192.168.1.156	jenkins.xa
192.168.1.160   gitea.xa
192.168.1.161   drone.xa
192.168.1.162	harbor.xa
```

### 2.1.3. 配置开机启动服务（Service）

以`Centos7`为例，创建`/lib/systemd/system/coredns.service`文件，配置如下：

```ini
[Unit]
Description=CoreDNS DNS server
Documentation=https://coredns.io
After=network.target

[Service]
PermissionsStartOnly=true
LimitNOFILE=1048576
LimitNPROC=512
CapabilityBoundingSet=CAP_NET_BIND_SERVICE
AmbientCapabilities=CAP_NET_BIND_SERVICE
NoNewPrivileges=true
ExecStart=/usr/local/bin/coredns -conf /etc/coredns/Corefile
ExecReload=/bin/kill -SIGUSR1 $MAINPID
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

配置开机启动

```shell
sudo systemctl enable coredns
```

启动和查看状态

```shell
sudo systemctl start coredns
sudo systemctl status coredns
```

### 2.1.4. 配置防火墙

如果防火墙是开启的，需要运行53端口可以访问，不但需要配置TCP还需要UDP，命令如下：

```shell
sudo firewall-cmd --zone=public --permanent --add-port=53/tcp
sudo firewall-cmd --zone=public --permanent --add-port=53/udp
sudo firewall-cmd --reload
```

### 2.1.5 测试配置是否正确

使用`dig`命令，验证域名解析是否正确。如果`dig`没有安装需要先安装。

```shell
sudo yum install bind-utils
```

验证命令如下：

```shell
dig @192.168.1.254 a vm.xa
```

输出如下：

```
; <<>> DiG 9.11.4-P2-RedHat-9.11.4-16.P2.el7_8.6 <<>> @192.168.1.254 a a vm.xa
; (2 servers found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 38740
;; flags: qr aa rd; QUERY: 1, ANSWER: 1, AUTHORITY: 0, ADDITIONAL: 1
;; WARNING: recursion requested but not available

;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;vm.xa.		IN	A

;; ANSWER SECTION:
vm.xa .	3600	IN	A	192.168.1.150

;; Query time: 0 msec
;; SERVER: ::1#53(::1)
;; WHEN: Fri Sep 11 01:18:25 EDT 2020
;; MSG SIZE  rcvd: 81
```

看到上面输入就说明配置完成了。在需要解析本地域名的电脑上配置就算完成了。

### 2.1.6. CoreDNS配置说明

| 参数      | 解释                                        |
| --------- | ------------------------------------------- |
| -conf     | 指定配置文件（默认为当前目录的 `Corefile`） |
| -dns.port | 指定 DNS 服务监听端口                       |
| -pidfile  | 指定 pid 文件的存放路径                     |
| -plugins  | 列出已安装的插件                            |
| -quiet    | 不打印日志                                  |
| -version  | 显示版本信息                                |

## 2.2. Docker安装

我们还将每个版本都作为Docker映像推送。您可以在CoreDNS的[Docker Hub](https://hub.docker.com/r/coredns/coredns/)找到它们



# 3. 参考文章

https://www.cnblogs.com/Dy1an/p/11157152.html

https://blog.csdn.net/wu_weijie/article/details/104781887

https://www.iamle.com/archives/2679.html


# 1. NFS是什么？
NFS（Network File System）即网络文件系统，它允许网络中的计算机之间通过TCP/IP网络共享资源。在NFS的应用中，本地NFS的客户端应用可以透明地读写位于远端NFS服务器上的文件，就像访问本地文件一样。

NFS的好处：节省本地存储空间，将常用的数据存放在一台NFS服务器上且可以通过网络访问，那么本地终端将可以减少自身存储空间的使用。

NFS体系有两个主要部分：

NFS服务端机器：通过NFS协议将文件共享到网络。

NFS客户端机器：通过网络挂载NFS共享目录到本地。

# 2. NFS安装与配置

NFS安装使用centos7系统。

## 2.1. 安装

安装NFS包，执行如下命令，nfs服务就安装完成了。

```shell
yum -y install nfs-utils
```

## 2.2. 配置

1. 编辑nfs共享文件目录

   ```shell
   vi /etc/exports
   ```

   添加配置信息如下：

   ```shell
   /home/nfs/dev 192.168.1.0/24(rw,sync,all_squash,anonuid=1000,anongid=1000)
   ```

   配置说明：

   | 配置            | 描述                                                         |
   | --------------- | ------------------------------------------------------------ |
   | /home/nfs/dev   | 指定共享的目录，如不指定，有权限的客户端可以挂载所有目录     |
   | 192.168.1.0/24  | 允许挂载目录的客户端地址，也可以定义一个网段192.168.234.0/24或者具体的IP地址 |
   | rw              | 可读可写，与之对应的配置为ro（只读）                         |
   | sync            | 同步模式，内存数据实时写入磁盘，与之对应的配置为async（非同步模式） |
   | no_root_squash  | 客户端挂载NFS共享目录后，root用户对该目录不受约束，权限很大  |
   | root_squash     | 与上面选项相对，客户端上的root用户受到约束，被限定成某个普通用户 |
   | all_squash      | 客户端上所有用户在使用NFS共享目录时都被限定为一个普通用户    |
   | anonuid/anongid | 和上面几个选项搭配使用，定义被限定用户的uid和gid（客户端访问NFS目录时，会变为uid为1000的普通用户） |

   

2. 创建nfs需要共享的目录。

   ```shell
   mkdir /home/nfs/dev
   chmod 777 /home/nfs/dev
   ```

   

3. 配置nfs端口

   nfs有3种端口需要在防火墙上配置:

   a).  `rpcbind`端口 111 udp/tcp

   b). `nfsd`端口 2049 udp/tcp

   c). `mountd`端口 "xxx" udp/tcp

   因为`mountd`是动态端口，系统 RPC服务在 nfs服务启动时默认会为`mountd`动态选取一个随机端口（32768--65535）来进行通讯，我们可以通过编辑`/etc/sysconfig/nfs`文件为 `mountd`指定一个固定端口。

   编辑`/etc/sysconfig/nfs`文件：

   ```shell
   #追加端口配置
   MOUNTD_PORT=4001　　
   STATD_PORT=4002
   LOCKD_TCPPORT=4003
   LOCKD_UDPPORT=4003
   RQUOTAD_PORT=4004
   ```

   

4. 配置防火墙

   ```shell
   sudo firewall-cmd --zone=public --permanent --add-port=111/tcp
   sudo firewall-cmd --zone=public --permanent --add-port=111/udp
   sudo firewall-cmd --zone=public --permanent --add-port=2049/tcp
   sudo firewall-cmd --zone=public --permanent --add-port=2049/udp
   sudo firewall-cmd --zone=public --permanent --add-port=4001-4004/tcp
   sudo firewall-cmd --zone=public --permanent --add-port=4001-4004/udp
   sudo firewall-cmd --reload
   ```

   

5. `rpcbind`和`nfs`开机启动配置

   ```shell
   systemctl enable rpcbind
   systemctl enable nfs
   ```

   

6. 启动`rpcbind`和`nfs`

   ```shell
   systemctl start rpcbind
   systemctl start nfs
   ```

7.  重新加载`etc/exports`文件

   ```shell
   # 卸载所有共享
   exportfs -au
   # 重挂所有共享
   exportfs -ar
   # 重新加载共享文件列表/etc/exports,生效设置
   exportfs -r
   # 查看共享目录
   exportfs -v
   ```

   

# 3. 验证nfs是否可以使用

在另外一台机器上验证nfs是否可用。

## 3.1. 安装`showmount`命令

如果已经安装可以忽略此步骤。

```shell
yum install showmount
```

## 3.2. 查看nfs共享目录

```shell
showmount -e 192.168.1.161
Export list for 192.168.1.161:
/home/nfs/test 192.168.1.0/24
/home/nfs/dev  192.168.1.0/24
```

可以看到nfs共享了两个目录`/home/nfs/test`和`/home/nfs/dev`。

## 3.3. 挂载nfs目录

```shell
mount -t nfs 192.168.1.161:/home/nfs/dev /home/dev
```

进入`/home/dev`目录创建一个文件`touch index.html`。然后到nfs服务对应的目录下看看文件是否存在也就是`192.168.1.161`的`/home/nfs/dev`目录下。如果存在说明nfs是没有问题的。

卸载挂载目录：

```shell
umount /home/dev
```

# 4. k8s使用

## 4.1 PV

```shell
apiVersion: v1
kind: PersistentVolume
metadata:
  name: nfs
spec:
  storageClassName: manual
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteMany
  nfs:
    server: 192.168.1.161
    path: "/home/nfs/dev"
```

## 4.2 PVC

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nfs
spec:
  accessModes:
    - ReadWriteMany
  storageClassName: manual
  resources:
    requests:
      storage: 1Gi
```

## pod使用

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: web-frontend
spec:
  containers:
  - name: web
    image: nginx
    ports:
      - name: web
        containerPort: 80
    volumeMounts:
        - name: nfs
          mountPath: "/usr/share/nginx/html"
  volumes:
  - name: nfs
    persistentVolumeClaim:
      claimName: nfs
```

# 5. 参考

https://blog.csdn.net/Powerful_Fy/article/details/102882620

https://www.cnblogs.com/cshaptx4869/p/12040862.html

https://www.cnblogs.com/cheyunhua/p/12033457.html
# 1. 什么是harbor？

Harbor是CNCF毕业项目，是容器镜像私服。可以保存docker镜像等。

# 2. 下载与安装

下载离线版本安装，[下载地址](https://github.com/goharbor/harbor/releases)

```shell
wget https://github.com/goharbor/harbor/releases/download/v2.1.0-rc2/harbor-offline-installer-v2.1.0-rc2.tgz
tar zxf harbor-offline-installer-v2.1.0-rc2.tgz -C /mnt
```

解压后的文件目录如下：

```
common.sh
harbor.v2.1.0.tar.gz
harbor.yml.tmpl
install.sh
LICENSE
prepare
```

其中`harbor.yml.tmpl`为配置文件，复制一份儿改名为`harbor.yml`

```yaml
# Configuration file of Harbor

# The IP address or hostname to access admin UI and registry service.
# DO NOT use localhost or 127.0.0.1, because Harbor needs to be accessed by external clients.
# 配置成自己的ip地址
hostname: reg.mydomain.com

# http related config
http:
  # port for http, default is 80. If https enabled, this port will redirect to https port
  # http端口
  port: 80

# https related config
https:
  # https port for harbor, default is 443
  # https 端口
  port: 443
  # The path of cert and key files for nginx
  # 为服务配置ssl证书
  certificate: /your/certificate/path
  private_key: /your/private/key/path

# # Uncomment following will enable tls communication between all harbor components
# internal_tls:
#   # set enabled to true means internal tls is enabled
#   enabled: true
#   # put your cert and key files on dir
#   dir: /etc/harbor/tls/internal

# Uncomment external_url if you want to enable external proxy
# And when it enabled the hostname will no longer used
# external_url: https://reg.mydomain.com:8433

# The initial password of Harbor admin
# It only works in first time to install harbor
# Remember Change the admin password from UI after launching Harbor.
# 管理员admin的密码
harbor_admin_password: Harbor12345

# Harbor DB configuration
# 数据库配置
database:
  # The password for the root user of Harbor DB. Change this before any production use.
  password: root123
  # The maximum number of connections in the idle connection pool. If it <=0, no idle connections are retained.
  max_idle_conns: 50
  # The maximum number of open connections to the database. If it <= 0, then there is no limit on the number of open connections.
  # Note: the default number of connections is 1024 for postgres of harbor.
  max_open_conns: 1000

# The default data volume
data_volume: /data

# Harbor Storage settings by default is using /data dir on local filesystem
# Uncomment storage_service setting If you want to using external storage
# storage_service:
#   # ca_bundle is the path to the custom root ca certificate, which will be injected into the truststore
#   # of registry's and chart repository's containers.  This is usually needed when the user hosts a internal storage with self signed certificate.
#   ca_bundle:

#   # storage backend, default is filesystem, options include filesystem, azure, gcs, s3, swift and oss
#   # for more info about this configuration please refer https://docs.docker.com/registry/configuration/
#   filesystem:
#     maxthreads: 100
#   # set disable to true when you want to disable registry redirect
#   redirect:
#     disabled: false

# Clair configuration
clair:
  # The interval of clair updaters, the unit is hour, set to 0 to disable the updaters.
  updaters_interval: 12

# Trivy configuration
#
# Trivy DB contains vulnerability information from NVD, Red Hat, and many other upstream vulnerability databases.
# It is downloaded by Trivy from the GitHub release page https://github.com/aquasecurity/trivy-db/releases and cached
# in the local file system. In addition, the database contains the update timestamp so Trivy can detect whether it
# should download a newer version from the Internet or use the cached one. Currently, the database is updated every
# 12 hours and published as a new release to GitHub.
trivy:
  # ignoreUnfixed The flag to display only fixed vulnerabilities
  ignore_unfixed: false
  # skipUpdate The flag to enable or disable Trivy DB downloads from GitHub
  #
  # You might want to enable this flag in test or CI/CD environments to avoid GitHub rate limiting issues.
  # If the flag is enabled you have to download the `trivy-offline.tar.gz` archive manually, extract `trivy.db` and
  # `metadata.json` files and mount them in the `/home/scanner/.cache/trivy/db` path.
  skip_update: false
  #
  # insecure The flag to skip verifying registry certificate
  insecure: false
  # github_token The GitHub access token to download Trivy DB
  #
  # Anonymous downloads from GitHub are subject to the limit of 60 requests per hour. Normally such rate limit is enough
  # for production operations. If, for any reason, it's not enough, you could increase the rate limit to 5000
  # requests per hour by specifying the GitHub access token. For more details on GitHub rate limiting please consult
  # https://developer.github.com/v3/#rate-limiting
  #
  # You can create a GitHub token by following the instructions in
  # https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line
  #
  # github_token: xxx

jobservice:
  # Maximum number of job workers in job service
  max_job_workers: 10

notification:
  # Maximum retry count for webhook job
  webhook_job_max_retry: 10

chart:
  # Change the value of absolute_url to enabled can enable absolute url in chart
  absolute_url: disabled

# Log configurations
log:
  # options are debug, info, warning, error, fatal
  level: info
  # configs for logs in local storage
  local:
    # Log files are rotated log_rotate_count times before being removed. If count is 0, old versions are removed rather than rotated.
    rotate_count: 50
    # Log files are rotated only if they grow bigger than log_rotate_size bytes. If size is followed by k, the size is assumed to be in kilobytes.
    # If the M is used, the size is in megabytes, and if G is used, the size is in gigabytes. So size 100, size 100k, size 100M and size 100G
    # are all valid.
    rotate_size: 200M
    # The directory on your host that store log
    location: /var/log/harbor

  # Uncomment following lines to enable external syslog endpoint.
  # external_endpoint:
  #   # protocol used to transmit log to external endpoint, options is tcp or udp
  #   protocol: tcp
  #   # The host of external endpoint
  #   host: localhost
  #   # Port of external endpoint
  #   port: 5140

#This attribute is for migrator to detect the version of the .cfg file, DO NOT MODIFY!
_version: 2.0.0

# Uncomment external_database if using external database.
# external_database:
#   harbor:
#     host: harbor_db_host
#     port: harbor_db_port
#     db_name: harbor_db_name
#     username: harbor_db_username
#     password: harbor_db_password
#     ssl_mode: disable
#     max_idle_conns: 2
#     max_open_conns: 0
#   clair:
#     host: clair_db_host
#     port: clair_db_port
#     db_name: clair_db_name
#     username: clair_db_username
#     password: clair_db_password
#     ssl_mode: disable
#   notary_signer:
#     host: notary_signer_db_host
#     port: notary_signer_db_port
#     db_name: notary_signer_db_name
#     username: notary_signer_db_username
#     password: notary_signer_db_password
#     ssl_mode: disable
#   notary_server:
#     host: notary_server_db_host
#     port: notary_server_db_port
#     db_name: notary_server_db_name
#     username: notary_server_db_username
#     password: notary_server_db_password
#     ssl_mode: disable

# Uncomment external_redis if using external Redis server
# external_redis:
#   # support redis, redis+sentinel
#   # host for redis: <host_redis>:<port_redis>
#   # host for redis+sentinel:
#   #  <host_sentinel1>:<port_sentinel1>,<host_sentinel2>:<port_sentinel2>,<host_sentinel3>:<port_sentinel3>
#   host: redis:6379
#   password:
#   # sentinel_master_set must be set to support redis+sentinel
#   #sentinel_master_set:
#   # db_index 0 is for core, it's unchangeable
#   registry_db_index: 1
#   jobservice_db_index: 2
#   chartmuseum_db_index: 3
#   clair_db_index: 4
#   trivy_db_index: 5
#   idle_timeout_seconds: 30

# Uncomment uaa for trusting the certificate of uaa instance that is hosted via self-signed cert.
# uaa:
#   ca_file: /path/to/ca

# Global proxy
# Config http proxy for components, e.g. http://my.proxy.com:3128
# Components doesn't need to connect to each others via http proxy.
# Remove component from `components` array if want disable proxy
# for it. If you want use proxy for replication, MUST enable proxy
# for core and jobservice, and set `http_proxy` and `https_proxy`.
# Add domain to the `no_proxy` field, when you want disable proxy
# for some special registry.
proxy:
  http_proxy:
  https_proxy:
  no_proxy:
  components:
    - core
    - jobservice
    - clair
    - trivy
```

配置上面加中文注释的，其他保持不变

`install.sh`为启动文件

# 3. 配置https证书

参考官网配置https://goharbor.io/docs/2.0.0/install-config/configure-https/

## 3.1 创建一个存放证书的目录

```shell
mkdir /mnt/certs
cd /mnt/certs
```

## 3.2 生成机构证书

1. 生成CA证书私钥

   ```shell
   openssl genrsa -out ca.key 4096
   ```

2. 生成CA证书

   ```shell
   openssl req -x509 -new -nodes -sha512 -days 3650 \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=harbor.xa" \
    -key ca.key \
    -out ca.crt
   ```

## 3.3 生成服务器证书

证书通常包含一个`.crt`文件和一个`.key`文件，例如`harbor.xa.crt`和`harbor.xa.key`。

1. 生成私钥

   ```shell
   openssl genrsa -out harbor.xa.key 4096
   ```

2. 生成证书签名请求（CSR）

   ```shell
   openssl req -sha512 -new \
       -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=harbor.xa" \
       -key harbor.xa.key \
       -out harbor.xa.csr
   ```

3. 生成一个x509 v3扩展文件

   ```shell
   cat > v3.ext <<-EOF
   authorityKeyIdentifier=keyid,issuer
   basicConstraints=CA:FALSE
   keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
   extendedKeyUsage = serverAuth
   subjectAltName = @alt_names
   
   [alt_names]
   DNS.1=harbor.xa
   DNS.2=harbor
   DNS.3=hostname
   EOF
   ```

4. 使用该`v3.ext`文件为您的Harbor主机生成证书

   ```shell
   openssl x509 -req -sha512 -days 3650 \
       -extfile v3.ext \
       -CA ca.crt -CAkey ca.key -CAcreateserial \
       -in harbor.xa.csr \
       -out harbor.xa.crt
   ```

## 3.4 提供证书给Harbor和Docker

生成后`ca.crt`，`harbor.xa.crt`和`harbor.xa.key`文件，必须将它们提供给`Harbor`和`Docker`，并重新配置`Harbor`的配置文件。

1. 将服务器证书和密钥复制到Harbor主机上的certficates文件夹中

2. 转换`harbor.xa.crt`为`harbor.xa.cert`，供Docker使用。Docker守护程序将`.crt`文件解释为CA证书，并将`.cert`文件解释为客户端证书

   ```shell
   openssl x509 -inform PEM -in harbor.xa.crt -out harbor.xa.cert
   ```

3. 将服务器证书，密钥和CA文件复制到Harbor主机上的Docker证书文件夹中。您必须首先创建适当的文件夹。

   ```
   cp harbor.xa.cert /etc/docker/certs.d/harbor.xa/
   cp harbor.xa.key /etc/docker/certs.d/harbor.xa/
   cp ca.crt /etc/docker/certs.d/harbor.xa/
   ```

   如果将默认`nginx`端口不是443端口，请创建文件夹`/etc/docker/certs.d/harbor.xa:port`或`/etc/docker/certs.d/harbor_IP:port`

4. 重新启动Docker Engine

   ```shell
   systemctl restart docker
   ```

   证书的配置目录如下：

   ```
   /etc/docker/certs.d/
       └── harbor.xa:port
          ├── harbor.xa.cert  <-- Server certificate signed by CA
          ├── harbor.xa.key   <-- Server key signed by CA
          └── ca.crt               <-- Certificate authority that signed the registry certificate
   ```

## 3.5 重新配置Harbor

如果Harbor还没部署，那就配置harbor.yml文件部署harbor，如果你已经使用http部署了Harbor，并希望将其重新部署为使用https，请执行以下步骤。

1. 运行`prepare`脚本以启动https。

   Harbor将`nginx`实例用作所有服务的反向代理。您可以使用`prepare`脚本来配置`nginx`为使用HTTPS。该`prepare`在港的安装包，在同级别的`install.sh`脚本。

   ```shell
   ./prepare
   ```

2. 如果Harbor正在运行，请停止并删除现有实例。

   您的镜像数据保留在文件系统中，因此不会丢失任何数据。

   ```sh
   docker-compose down -v
   ```

3. 重启Harbor：

   ```sh
   docker-compose up -d
   ```

## 3.6 验证HTTPS连接

为Harbor设置HTTPS之后，您可以通过执行以下步骤来验证HTTPS连接。

- 打开浏览器，然后输入[https://harbor.xa](https://harbor.xa)。它应该显示Harbor界面。

  某些浏览器可能会显示警告，指出证书颁发机构（CA）未知。使用不是来自受信任的第三方CA的自签名CA时，会发生这种情况。您可以将CA导入浏览器以消除警告。

- 在运行Docker守护程序的机器上，检查`/etc/docker/daemon.json`文件以确保`-insecure-registry`未为[https://harbor.xa](https://harbor.xa/)设置该选项。

- 从Docker客户端登录Harbor。

  ```sh
  docker login harbor.xa
  ```

  如果已将`nginx`443端口映射到其他端口，请在`login`命令中添加该端口。

  ```sh
  docker login harbor.xa:port
  ```

# 参考网站

https://www.cnblogs.com/sanduzxcvbnm/p/11956347.html
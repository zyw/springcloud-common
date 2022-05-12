# 配置文件介绍
1. `docker-compose.yaml`使用`docker-compose`安装`Elasticsearch`集群和`kibana`。
2. `elasticsearch.yml` `Elasticsearch`配置文件。
3. `kibana.yml`配置文件

# 在宿主机上执行docker容器中的命令
`es7173_01`容器名称
```shell
# 设置密码
docker exec -it es7173_01 /usr/share/elasticsearch/bin/elasticsearch-setup-passwords interactive
# 集群创建一个证书颁发机构
docker exec -it es7173_01 /usr/share/elasticsearch/bin/elasticsearch-certutil ca
# 集群创建一个证书颁发机构
docker exec -it es7173_01 /usr/share/elasticsearch/bin/elasticsearch-certutil cert --ca elastic-stack-ca.p12
```

# `Elasticsearch`安全配置
`Elasticsearch`安全配置分为三个级别：
## 1、最低限度的安全性（开发环境）
* 在`elasticsearch.yml`配置文件中添加如下配置
  ```yaml
   xpack.security.enabled: true
  ```
* 启动`Elasticsearch`服务，然后运行一下命令，设置`apm_system`、`beats_system`、`elastic`、`kibana`、`kibana_system`、`logstash_system`、`remote_monitoring_user`用户密码。
  ```shell
   bin/elasticsearch-setup-passwords interactive
  ```
  在集群环境下设置了这个属性后重启`Elasticsearch`就会失败。
* 配置kibana.yml文件
  生成密码后需要在kibana中配置.
  ```yaml
  elasticsearch.username: "kibana_system"
  elasticsearch.password: "12345678"
  ```
## 2、基础安全（生产环境）
在集群环境下各个节点数据传输需要加密传输需要配置`elasticsearch.yml`如下：
```yaml
xpack.security.transport.ssl.enabled: true
xpack.security.transport.ssl.verification_mode: certificate
xpack.security.transport.ssl.client_authentication: required
xpack.security.transport.ssl.keystore.path: elastic-certificates.p12
xpack.security.transport.ssl.truststore.path: elastic-certificates.p12
```
其中`elastic-certificates.p12`为证书，通过如下命令生成证书：
1. 为E`lasticsearch`集群创建一个证书颁发机构，会生成一个`elastic-stack-ca.p12`文件。可以设置密码也可以使用空密码，如果使用密码要记住，后面要用。
```shell
./bin/elasticsearch-certutil ca
```
2. 为群集中的每个节点生成证书和私钥，生成一个`elastic-certificates.p12`文件。可以设置密码也可以使用空密码，如果使用密码要记住，后面要用。
```shell
./bin/elasticsearch-certutil cert --ca elastic-stack-ca.p12
```
3. 如果您在创建节点证书时输入了密码，请运行以下命令将密码存储在`Elasticsearch`密钥库中
```shell
./bin/elasticsearch-keystore add xpack.security.transport.ssl.keystore.secure_password

./bin/elasticsearch-keystore add xpack.security.transport.ssl.truststore.secure_password
```
## 3、基本安全加上安全的HTTPS传输
[基本安全加上安全的HTTPS传输](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/security-basic-setup-https.html)
上面有配置elasticsearch节点之间和kibana到es节点，Logstash到ES节点的https请求，还有浏览器访问kibana的https自签证书的配置
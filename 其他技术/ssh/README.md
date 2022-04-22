# 免密登录本机
1.第一步产生公钥和私钥，一路回车就可以了
```shell script
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
```
执行上面后会产生`id_rsa`(私钥)和`id_rsa.pub`(公钥)
2.第二步创建`authorized_keys`
```shell script
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```
3.第三步
```shell script
chmod 0600 ~/.ssh/authorized_keys
```
# 密码登录别的主机
执行如下：
```shell script
ssh-copy-id -i 主机名或者主机IP
```
也可以指定公钥
```shell script
ssh-copy-id -i ~/.ssh/id_rsa.pub username@ip
```
# CentOS 7

## 一、防火墙
1. 查看状态`firewall-cmd --state`
2. 停止防火墙`systemctl stop firewalld.service`
3. 禁止firewall开机启动`systemctl disable firewalld.service`
4. 添加端口`firewall-cmd --permanent --zone=public --add-port=80/tcp`或者`firewall-cmd --permanent --zone=public --add-service=http`
5. 重新load`firewall-cmd --reload`
6. 检测是否生效`firewall-cmd --zone=public --query-port=80/tcp`
7. 列出所有的开放端口`firewall-cmd --list-all`
8. 删除端口`firewall-cmd --zone=public --remove-port=80/tcp`

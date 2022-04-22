## minio集群搭建
```shell script
./minio server http://192.168.33.12/home/vagrant/minio/data1 http://192.168.33.12/home/vagrant/minio/data2 http://192.168.33.17/home/vagrant/minio/data1 http://192.168.33.17/home/vagrant/minio/data2
```

## docker简单部署
[docker简单部署](https://docs.min.io/docs/minio-docker-quickstart-guide.html)
1. 默认key
```shell script
docker run -p 9000:9000 --name minio1 \
  -v /mnt/data:/data \
  minio/minio server /data
```
2. 设置key
```shell script
docker run -p 9000:9000 --name minio1 \
  -e "MINIO_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE" \
  -e "MINIO_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" \
  -v /mnt/data:/data \
  minio/minio server /data
```

## docker集群部署
[docker集群部署](https://docs.min.io/docs/distributed-minio-quickstart-guide.html)

## vagrant启动的两个主机间免密登录配置
需要开启`/etc/ssh/sshd_config`文件中的`PasswordAuthentication yes`前面的注释
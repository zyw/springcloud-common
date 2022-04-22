## Docker部署admin
```shell script
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://192.168.31.213:3306/xxl-job?Unicode=true&characterEncoding=UTF-8&useSSL=false --spring.datasource.username=root --spring.datasource.password=root" \
-p 8080:8080 -v /tmp:/data/applogs --name xxl-job-admin \
-d xuxueli/xxl-job-admin:2.1.2
```
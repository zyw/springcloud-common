## 使用gitlab的ci/cd持续集成和持续构件项目

### 使用docker方式安装gitlab
[官方地址](https://docs.gitlab.com/omnibus/docker/)

### gitlab runner安装
gitlab runner分为docker安装和非docker安装
[gitlab runner的安装与配置](https://zhuanlan.zhihu.com/p/51163261)
[gitlab executors](https://docs.gitlab.com/runner/executors/README.html)
#### gitlab runner config.toml配置
```
concurrent = 1
check_interval = 0

[session_server]
  session_timeout = 1800

[[runners]]
  name = "powertime"
  url = "http://192.168.1.23/"
  token = "qpJ79UVGjSNqEqpKQTxY"
  executor = "docker"
  [runners.custom_build_dir]
  [runners.docker]
    tls_verify = false
    image = "maven:3-jdk-8"
    privileged = true
    disable_entrypoint_overwrite = false
    oom_kill_disable = false
    disable_cache = false
    volumes = ["/mnt/maven:/root/.m2","/var/run/docker.sock:/var/run/docker.sock","/data/gitlab-runner:/gitlab", "/data/maven_repo:/data/maven", "/data/gradle:/data/gradle", "/data/sonar_cache:/root/.sonar", "/data/androidsdk:/usr/local/android", "/data/node_modules:/data/node_modules"]
    # 知道映射的ip或域名
    extra_hosts = ["git.mritd.me:172.16.0.37"]
    shm_size = 0
  [runners.cache]
    [runners.cache.s3]
    [runners.cache.gcs]

[[runners]]
  name = "ssh"
  url = "http://192.168.1.23/"
  token = "bmHmBVPHih15pJCBAfEJ"
  executor = "ssh"
  [runners.custom_build_dir]
  [runners.ssh]
    user = "root"
    password = "powertime"
    host = "192.168.1.20"
    port = "22"
  [runners.cache]
    [runners.cache.s3]
    [runners.cache.gcs]

[[runners]]
  name = "docker-ssh"
  url = "http://192.168.1.23/"
  token = "yTmibv7pZHtpxUF4tvP3"
  executor = "docker-ssh"
  [runners.custom_build_dir]
  [runners.ssh]
    user = "root"
    password = "powertime"
  [runners.docker]
    tls_verify = false
    image = "ubuntu:16.04"
    privileged = false
    disable_entrypoint_overwrite = false
    oom_kill_disable = false
    disable_cache = false
    volumes = ["/cache"]
    shm_size = 0
  [runners.cache]
    [runners.cache.s3]
    [runners.cache.gcs]

[[runners]]
  name = "shell"
  url = "http://192.168.1.23/"
  token = "NfUdAJK5_zgys8hXmpxy"
  executor = "shell"
  [runners.custom_build_dir]
  [runners.cache]
    [runners.cache.s3]
    [runners.cache.gcs]
```

### gitlab.rb
`gitlab.rb`为gitlab的配置文件，里面配置了gitlab的访问域名和`registry_external_url`的访问域名
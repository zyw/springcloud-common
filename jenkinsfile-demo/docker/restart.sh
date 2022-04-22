#!/usr/bin/env bash
 # 定义应用名称
 app_name='jenkinsfile-demo'

 docker restart ${app_name}
 echo '----restart container----'
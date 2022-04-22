## 说明
在根目录下创建一个config目录，里面包含一个`application.properties`或者`application.yml`文件`Spring Boot`会自动加载该配置文件的。
## 依据
`SpringApplication` loads properties from `application.properties` files in the following locations and adds them to the Spring `Environment`:

1. A `/config` subdirectory of the current directory
2. The current directory
3. A classpath `/config` package
4. The classpath root

The list is ordered by precedence (properties defined in locations higher in the list override those defined in lower locations).

> You can also use YAML ('.yml') files as an alternative to '.properties'.

## docker安装Elasticsearch

```shell
docker run --name elasticsearch --restart always \
-c "sh elasticsearch-plugin install https://github.com/KennFalcon/elasticsearch-analysis-hanlp/releases/download/v7.5.1/elasticsearch-analysis-hanlp-7.5.1.zip 
   && elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.5.1/elasticsearch-analysis-ik-7.5.1.zip 
   && elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-pinyin/releases/download/v7.5.1/elasticsearch-analysis-pinyin-7.5.1.zip" \
-v /mnt/elasticsearch:/usr/share/elasticsearch/data \
-p 9200:9200 -p 9300:9300 -d docker.elastic.co/elasticsearch/elasticsearch:7.5.1
```
## docker-compose安装
### 1. 安装
```shell script
curl -SL https://github.com/docker/compose/releases/download/v2.5.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
// 过期
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose;
```
### 2. 授权
```shell
sudo chmod +x /usr/local/bin/docker-compose
```
### 3. 软连接（可选）
```shell
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

## 正排索引和倒排索引的定义

### 正排索引

从id或者页码到文档，称为 **正排索引**

### 倒排索引

从文档到id或者页码，称为**倒排索引**

## Lucene（Elasticsearch）中数据定义

1. Document(文档)
   Elasticsearch是面向文档的，文档是所有可搜索数据的最小单位，相当于数据库中一行记录。文档会被序列化成JSON格式，保存到ES中，每个文档都有一个ID(Unique ID)可以自定义也可以由ES生成。
   每个文档都有一些元数据用于标注文档的相关信息：
   
   * _index: 文档所属的索引名
   * _type: 文档所属的类型名
   * _id: 文档唯一的ID
   * _source: 文档的原始JSON数据
   * _version: 文档的版本信息
   * _score: 相关性打分
2. index(索引)
   1). 索引是文档的容器，是一类文档的结合
   * Index体现了逻辑空间的概念：每个索引都有自己的Mapping定义，用于定义包含的文档的字段名和字段类型
   * Shard体现了物理空间的概念：索引中的数据分散在Shard上
   2). 索引的Mapping与Settings
   * Mapping定义文档字段的类型(相当于数据库的建表SQL(Schema))
   * Setting定义不同的数据分布
3. ES与数据库对比

   |       RDBMS      | Elasticsearch |
   | ---------------- | ------------- | 
   |       Table      | Index(Type)   |
   |       Row        | Document      |
   |       Column     | Filed         |
   | Schema(建表SQL)  | Mapping       |
   |       SQL        | ES查询语言(DSL)|

## Elasticsearch节点(node)角色
```yaml
node.roles: [ data, master, voting_only ]
```
node.roles您可以通过设置来定义节点的角色在elasticsearch.yml中。如果您设置node.roles，则仅向节点分配您指定的角色。如果不设置node.roles，则为节点分配以下角色：
* master
* data
* data_content
* data_hot
* data_warm
* data_cold
* data_frozen
* ingest
* ml
* remote_cluster_client
* transform

### Master-eligible node

具有`master`角色的节点，使其有资格被选为控制集群 的主节点。

### Data node

具有`data`角色的节点。数据节点保存数据并执行数据相关操作，例如 CRUD、搜索和聚合。具有该`data`角色的节点可以填充任何专门的数据节点角色。

### Ingest node

ingest 节点可以看作是数据前置处理转换的节点，支持 pipeline管道 设置，可以使用 ingest 对数据进行过滤、转换等操作，类似于 logstash 中 filter 的作用，功能相当强大。

[Elasticsearch的ETL利器——Ingest节点](https://juejin.cn/post/6844903873153335309)

### Remote-eligible node

`remote_cluster_client`使其有资格充当远程客户端

### Machine learning node

具有`ml`角色的节点。如果要使用机器学习功能，集群中必须至少有一个机器学习节点。有关更多信息，请参阅 [Elastic Stack 中的](https://www.elastic.co/guide/en/machine-learning/7.17/index.html)[机器学习设置](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/ml-settings.html)和机器学习。

### Transform node

具有`transform`角色的节点。如果要使用变换，集群中必须至少有一个变换节点。有关详细信息，请参阅 [转换设置](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/transform-settings.html)和[*转换数据*](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/transforms.html)。

## Elasticsearch7.5.1 docker-compose安装ES
```yaml
version: '2.2'
services:
  cerebro:
    image: lmenezes/cerebro:0.8.5
    container_name: cerebro
    ports:
      - "9000:9000"
    command:
      - -Dhosts.0.host=http://elasticsearch:9200
    networks:
      - es7net
  kibana:
    image: docker.elastic.co/kibana/kibana:7.5.1
    container_name: kibana7
    environment:
      - I18N_LOCALE=zh-CN
      - XPACK_GRAPH_ENABLED=true
      - TIMELION_ENABLED=true
      - XPACK_MONITORING_COLLECTION_ENABLED="true"
    ports:
      - "5601:5601"
    networks:
      - es7net
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.5.1
    container_name: es7_01
    environment:
      - cluster.name=geektime
      - node.name=es7_01
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - discovery.seed_hosts=es7_01,es7_02
      - cluster.initial_master_nodes=es7_01,es7_02
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - es7data1:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - es7net
  elasticsearch2:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.5.1
    container_name: es7_02
    environment:
      - cluster.name=geektime
      - node.name=es7_02
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - discovery.seed_hosts=es7_01,es7_02
      - cluster.initial_master_nodes=es7_01,es7_02
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - es7data2:/usr/share/elasticsearch/data
    networks:
      - es7net


volumes:
  es7data1:
    driver: local
  es7data2:
    driver: local

networks:
  es7net:
    driver: bridge
```
### 安装ES异常
1. vm.max_map_count
```text
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```
解决：
先要切换到root用户；
然后可以执行以下命令，设置 vm.max_map_count ，但是重启后又会恢复为原值。
```shell script
sysctl -w vm.max_map_count=262144
```
持久性的做法是在 /etc/sysctl.conf 文件中修改 vm.max_map_count 参数：
```shell script
echo "vm.max_map_count=262144" > /etc/sysctl.conf
sysctl -p
```

## 索引创建和查询

### 基本操作

```htaccess
# 参看索引mapping
GET _mapping
# 查看指定索引setting
GET kibana_sample_data_flights/_settings
# 查看指定索引mapping
GET kibana_sample_data_flights/_mapping

# 查看插件
GET /_cat/plugins

# 查看索引模板
GET /_cat/templates
# 查看机器健康状况
GET _cluster/health
# 查看分片
GET _cat/shards
# 查看节点
GET /_cat/nodes?v
```

### 文档的CRUD

* Type名约定都用_doc
* Index-如果ID不存在，创建新的文档。否则，先删除现有的文档，再创建新的文档，版本会增加
* Create - 如果ID已经存在，会失败
* Update-文档必须已经存在，更新只会对相应字段做增量修改

```htaccess
# Index
PUT my_index/_doc/1
{
	"name": "zhangsan",
	"age": 12
}
# Create
PUT my_index/_create/1
{
	"name": "zhangsan",
	"age": 12
}
POST my_index/_doc # 不指定ID,自动生成
{
	"name": "zhangsan",
	"age": 12
}
# Read
GET my_index/_doc/1
# Update
POST my_index/_update/1
{
	"doc":{
        "name": "lisi",
        "age": 20
	}
}
# Delete
DELETE my_index/_doc/1
```

### Bulk API

* 支持在一次API调用中，对不同的索引进行操作
* 支持四种类型的操作：Index、Create、Update、Delete。
* 可以再URI中指定Index,也可以在请求的Payload中进行
* 操作中单条操作失败，并不会影响其他操
* 返回结果包括了每一条操作执行的结果

```htaccess
POST _bulk
{ "index" : {"_index":"test","_id":"1" } }
{ "field1" :"value1" }
[ "delete": {"_index":"test","_id":"2" } }
{ "create": {"_index":"test2","_id":"3" } }
{ "field1":"value3" }
{ "update" : { "_id":"1","_index":"test" } }
{ "doc": {"field2":"value2" } }
```

### 批量读取 mget

跟Bulk API类似

```htaccess
GET _mget
{
 "doc": [
 	{
 		"_index": "user",
 		"_id": 1
 	},
 	{
 		"_index": "comment",
 		"_id": 1
 	}
 ]
}
```

### 批量查询 msearch

```htaccess
POST users/_msearch
{}
{"query": {"match_all": {}}, "from": 0, "size": 10}
{}
{"query": {"match_all": {}}}
{"index": "twitter2" }
{"query": {"match_all": {}}}
```

## 分词

### Analysis 与 Analyzer

* Analysis-文本分析是把全文本转换一系列单词（term/token)的过程，也叫分词
* Analysis 是通过 Analyzer 来实现的
  * 可使用 Elasticsearch内置的分析器/或者按需定制化分析器
* 除了在数据写入时转换词条，匹配Query语句时候也需要用相同的分析器对查询语句进行分析

### Analyzer的组成

分词器是专门处理分词的组件，Analyzer由三部分组成：

* Character Filters(针对原始文本处理，例如去除html)
*  Tokenizer(按照规则切分为单词)
* Token Filter(将切分的的单词进行加工，小写，删除 stopwords,增加同义词)

### 内置分词器

* Standard Analyzer一默认分词器，按词切分，小写处理
* Simple Analyzer 一按照非字母切分(符号被过滤)，小写处理
* Stop Analyzer 一小写处理，停用词过滤(the,a，is)
* Whitespace Analyzer一按照空格切分，不转小写
* Keyword Analyzer 一不分词，直接将输入当作输出
* Patter Analyzer 一正则表达式，默认 \W+(非字符分隔)
* Language 一提供了30多种常见语言的分词器
* Customer Analyzer自定义分词器

## Search API

### URL Search

在URL中使用查询参数

#### 指定字段V.S泛查询

```htaccess
# 带profile
GET /movies/_search?q=2012&df=title
{
  "profile":"true"
}

# 泛查询，正对_all,所有字段
GET /movies/_search?q=2012
{
  "profile":"true"
}

# 指定字段
GET /movies/_search?q=title:2012
{
 "profile" :"true"
}
```

#### Term v.s Phrase

* Beautiful Mind 等效于 Beautiful OR Mind
* "Beautiful Mind",等效于 Beautiful AND Mind。Phrase查询，还要求前后顺序保持一致

```htaccess
//使用引号，Phrase查询
GET /movies/_search?q=title:"Beautiful Mind"
{
"profile":"true"
}
//查找美丽心灵，Mind为泛查询
GET /movies/_search?q=title:Beautiful Mind
{
"profile":"true"
}
//分组，Bool查询
GET /movies/_search?q=title:(Beautiful Mind)
{
"profile":"true"
}
```



#### 分组与引号

* title:(Beautiful AND Mind)
* title="Beautiful Mind"

#### 布尔操作

* AND / OR / NOT 或者 && / ll / !
  * 必须大写
  * title:(matrix NOT reloaded)

#### 分组

* +表示must
* -表示must_not
* title:(+matrix-reloaded)

```htaccess
//查找美丽心灵
GET /movies/_search?q=title:(Beautiful AND Mind)
{
"profile":"true"
}
//查找美丽心灵
GET / movies/_ search?q = title:( Beautiful NOT Mind )
{
"profile":"true"
}
//查找美丽心灵 %2表示URL中的+
GET /movies/_search?q=title:(Beautiful %2BMind)
{
"profile":"true"
}
```

#### 范围查询

区间表示：[]闭区间，{}开区间

* year: {2019 TO 2018}
* year:[* TO 2018]

#### 算数符合

* year:>2010
* year:(>2010 && <=2018)
* year:(+>2010 +<=2018)

```htaccess
//范围查询，区间写法/数学写法
GET /movies/_search?q=year:>=1980
{
"profile":"true"
}
```

#### 通配符查询

通配符查询（通配符查询效率低，占用内存大，不建议使用。特别是放在最前面）

* ？代表1个字符，*代表0或多个字符
  * title:mi?d
  * title:be*

#### 正则表达式

title:[bt]oy

#### 模糊匹配与近似查询

* title:befutifl~1
* title:"lord rings"~2

```htaccess
//通配符查询
GET /movies/_ search?q=title:b*
{
"profile":"true"
}
//模糊匹配&近似度匹配
GET /movies/_search?q=title:beautifl~1
{
"profile":"true"
}
GET /movies/_search?q=title:"Lord Rings"~2
{
"profile":"true"
}
```



### Request Body Search

使用Elasticsearch提供的，基于JSQN格式的更加完备的Query Domain Specific Language (DSL)

| 语法                     | 范围              |
| ------------------------ | ----------------- |
| _search                  | 集群上所有的索引  |
| /index-1/_search         | index-1           |
| /index-1,index-2/_search | index-1,index-2   |
| /index-*/_search         | index-*开头的索引 |

#### Query DSL特性

* 分页 `from: 10, size: 20`

* 排序 `sort:[{"id":"desc"}]`

* _source filtering `"_source":["id","name"]`

* 脚本字段

  ```htaccess
  # 脚本字段
  GET kibana_sample_data_ecommerce/_search
  {
      "script_fields":{
      	"new_field":{
      		"script":{
      			"Lang":"painless",
      			"source":"doc['order_date'].value +'_hello'"
      		}
      	}	
      },
      "query":{
        "match_all":{}
      }
  }
  ```

#### 使用查询表达式 - Match

```htaccess
# comment字段中的两个词是OR的关系
GET /comments/_doc/_search
{
	"query":{
		"match":{
    		"comment":"Last Christmas"
 		}
	}
}

# 如果要使用AND关系，请使用一下方法
GET /comments/_doc/_search
{
	"query":{
		"match":{
			"comment":{
				"query":"Last Chrismas",
				"operator":"AND"
			}
		}
	}
}

```

#### 短语搜索 - Match Phrase

```htaccess
# 短语查询query的语句看成一个整体去查询，每个词按照顺序出现，slop设置成1，表示短语中级可以有一个其他字符
GET /comments/_doc/_search
{
   "query":{
       "match_phrase":{
           "comment":{
               "query":"Song Last Chrismas",
               "slop":1
            }
         }
     }
}
```

#### Query String Query

类似URI Query

```htaccess
POST users/_search
{
	"query": {
		"query_string": {
			"default_field": "name",
			"query": "Ruan AND Yiming"
		}
	}
}

POST user/_search
{
	"query": {
		"query_string": {
			"fields": ["name", "about"],
			"query": "(Ruan AND Yiming) OR (Java AND Elasticsearch)"
		}
	}
}
```

#### Simple Query String Query

* 类似Query String,但是会忽略错误的语法，同时只支持部分查询语法
* 不支持AND OR NOT,会当作字符串处理
* Term 之间默认的关系是OR,可以指定Operator
* 支持 部分逻辑
  * \+ 替代 AND
  * | 替代 OR
  * \- 替代 NOT

```htaccess
POST user/_search
{
	"query": {
		"simple_query_string": {
			"fields": ["name", "about"],
			"query": "Ruan -Yiming",
			"default_operator": "AND"
		}
	}
}
```

## Mapping

### 什么是Mapping

1. Mapping类似数据库中的schema的定义，作用如下
   * 定义索引中的字段的名称
   * 定义字段的数据类型，例如字符串，数字，布尔...
   * 字段，倒排索引的相关配置，（Analyzed or Not Analyzed）
2. Mapping会把JSON文档映射成Lucene所需要的扁平格式
3. 一个Mapping属于一个索引的Type
   * 每个文档都属于一个Type
   * 一个Type有一个Mapping定义
   * 7.0开始，不需要在Mapping定义中指定type信息

### 字段的数据类型

1. 简单类型
   * Text / Keyword
   * Date
   * Integer / Floating
   * Boolean
   * IPv4 & IPv6
2. 复杂类型 - 对象和嵌套对象
   * 对象类型 / 嵌套类型
3. 特殊类型
   * geo_point & geo_shape / percolator

### 什么是 Dynamic Mapping

* 在写入文档时候，如果索引不存在，会自动创建索引
* Dynamic Mapping 的机制，使得我们无需手动定义Mappings。Elasticsearch 会自动根据文档信息，推算出字段的类型
* 但是有时候会推算的不对，例如地理位置信息
* 当类型如果设置不对时，会导致一些功能无法正常运行，例如Range查询

### 能否更改Mapping的字段类型

#### 新增加字段

1. Dynamic设为true时，一旦有新的字段写入文档，Mapping也同时被更新。
2. Dynamic设为false时，Mapping不会被更新，新增字段的数据无法被索引，但是是有信息会出现在_source中
3. Dynamic设成Strict，文档写入失败

#### 对已有字段

Lucene实现的倒排索引，一旦生成后，就不允许修改。

如果希望改变字段类型，必须Reindex API，重建索引

### 控制 Dynamic Mapping

```htaccess
# 设置Dynamic
PUT users
{
	"mappings": {
		"_doc": {
			"dynamic": "false"
		}
	}
}
```

|               | true | false | strict |
| ------------- | ---- | ----- | ------ |
| 文档可索引    | YES  | YES   | NO     |
| 字段可索引    | YES  | NO    | NO     |
| Mapping被更新 | YES  | NO    | NO     |

### 显示定义一个Mapping

```htaccess
PUT users
{
	"mappings": {
		// 定义自己的mappings
	}
}
```

#### Index - 控制字段是否被索引

Index - 控制当前字段是否被索引。默认为true。如果设置成 false,该字段不可被搜索

```htaccess
PUT users
{
	"mappings": {
		"properties" : {
			"firstName" : {
				"type" : "text",
				"index" : false,
				"index_options": "offsets"
			}
		}
	}
}
```

##### Index Options

1. 四种不同级别的Index Options配置，可以控制倒排索引记录的内容

* docs - 记录 doc id
* freqs - 记录 doc id和term frequencies
* positions - 记录doc id / term frequencies / term position
* offsets - doc id / term frequencies / term position / character offsets

2. Text类型默认记录postions，其他默认为docs

3. 记录内容越多，占用存储空间越大

#### null_value

需要对null值实现搜索，只有keyword类型支持设定null_value

```htaccess
PUT users
{
	"mappings": {
		"properties" : {
			"firstName" : {
				"type" : "keyword",
				"null_value" : "NULL"
			}
		}
	}
}
```

### 多字段类型（一个字段多个类型）

1. 厂商名称实现精确匹配，增加一个keyword字段。
2. 使用不同的analyzer，不同语言，pingyin字段的搜索，还支持为搜索和索引指定不同的analyzer

### Exact Values vs Full Text

* Exact Value: 包括数字 / 日期 / 具体一个字符串（例如：“Apple Store”苹果商店做为一个整体，不需要分词）需要设置成keyword类型，精确值不需要被分词
* 全文本，非结构化的文本数据，在ES中指定为text类型，需要分词索引

### 什么是 Index Template

Index Templates - 帮助你设定 Mappings 和 Settings,并按照一定的规则，自动匹配到新创建的索引之上。模版仅在一个索引被新创建时，才会产生作用。修改模版不会影响已创建的索引。

```htaccess
# 创建所有索引默认使用的索引模板
PUT _template/template_default
{
	"index_patterns" : ["*"],
	"order" : 0,
	"version" : 1,
	"settings" : {
		"number_of_shards" : 1,
		"number_of_replicas" : 1
	}
}
# 所有以test开头的索引使用的索引模板
PUT /_template/template_test
{
	"index_patterns" : ["test*"],
	"order" : 1,
	"settings" : {
		"number_of_shards" : 1,
		"number_of_replicas" : 1
	},
	"mappings" : {
		"date_detection" : false, //字符串转日期
		"numeric_detaction" : true  //字符串转数字
	}
}
```

### 什么是Dynamic Template

根据Elasticsearch识别的数据类型，结合字段名称，来动态设定字段类型

```htaccess
PUT my_test_index
{
	"mappings":{
		"dynamic_templates":[
			{
				"full_name":{
					"path_match": "name.*",
					"path_unmatch":"*.middle",
					"mapping":{
						"type":"text",
						"copy_to":"full_name"
					}
				}
			}
		]
	}
}

```

* Dynamic Tempate 是定义在在某个索引的 Mapping 中
* Template有一个名称
* 匹配规则是一个数组
* 为匹配到的字段设置Mapping



```json
#ik_max_word
#ik_smart
#Analyzer: ik_smart , ik_max_word , Tokenizer: ik_smart , ik_max_word

#hanlp: hanlp默认分词
#hanlp_standard: 标准分词
#hanlp_index: 索引分词
#hanlp_nlp: NLP分词
#hanlp_n_short: N-最短路分词
#hanlp_dijkstra: 最短路分词
#hanlp_crf: CRF分词（在hanlp 1.6.6已开始废弃）
#hanlp_speed: 极速词典分词

POST _analyze
{
  "analyzer": "ik_smart",
  "text": ["剑桥分析公司多位高管对卧底记者说，他们确保了唐纳德·特朗普在总统大选中获胜"]

}

# 查看索引模板
GET /_cat/templates

# 查看索引相关信息
GET kibana_sample_data_flights
# 查看索引包含的文档总数
GET kibana_sample_data_flights/_count

# 创建索引
PUT /security-evaluation-v35
{
    "settings" : {
      "analysis": {
        "analyzer": {
          
          "hanlp": {
            "tokenizer": "hanlp_standard"
          },
          "pinyin": {
            "tokenizer": "pinyin"
          }
        }
      }
    }
}
# 查看索引
GET /security-evaluation-v2/_search
{
  "query": {
    "match": {
      "title.pinyin": "biaoti"
    }
  }
}

# 多条件查询
GET /security-evaluation-v2/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "type": "3"
        }}
      ], 
      "should": [
        {
          "match": { 
            "title": "zhangsan"
          }
        },
        {
          "match": {
            "name": "zhangsan"
          }
        },
        {
          "match": {
            "content": "zhangsan"
          }
        },
        {
          "match": {
            "title.pinyin": "zhangsan"
          }
        },
        {
          "match": {
            "name.pinyin": "zhangsan"
          }
        }
      ]
    }
  }, 
  "from": 0,
  "size": 20
}

# 删除索引
DELETE /security-evaluation-v1

# 查询索引信息
GET /security-evaluation-v2

# 查看该索引下字段是如何分词的
GET /security-evaluation-v35/_analyze
{
  "field": "name", 
  "text":"张三2"
}

#插入数据 PUT 插入必须指定id,POST可以自动生成id
PUT /security-evaluation-v2/_doc/3
{
  "content" : "在古老的 Hadoop1.0 中，MapReduce 的 JobTracker 负责了太多的工作，包括资源调度，管理众多的 TaskTracker 等工作。这自然是不合理的，于是 Hadoop 在 1.0 到 2.0 的升级过程中，便将 JobTracker 的资源调度工作独立了出来，而这一改动，直接让 Hadoop 成为大数据中最稳固的那一块基石。，而这个独立出来的资源管理框架，就是 Yarn",
    "createTime" : "2020-01-18 14:04:34",
    "fileId" : "1123455_13",
    "id" : 15795070137913,
    "name" : "赵六",
    "status" : "完成30%",
    "title" : "深入浅出 Hadoop YARN",
    "type" : 2
}

#创建mapping
PUT /security-evaluation-v2/_mapping
{
  "properties" : {
    "createTime" : {
      "type" : "text",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        }
      }
    },
    "fileId" : {
      "type" : "keyword",
      "index" : false
    },
    "id" : {
      "type" : "long"
    },
    "name" : {
      "type" : "text",
      "analyzer": "hanlp",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        },
        "pinyin" : {
          "type" : "text",
          "analyzer" : "pinyin"
        }
      }
    },
    "content" : {
      "type" : "text",
      "analyzer": "hanlp",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        }
      }
    },
    "status" : {
      "type" : "text",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        }
      }
    },
    "title" : {
      "type" : "text",
      "analyzer": "hanlp",
      "fields" : {
        "keyword" : {
          "type" : "keyword",
          "ignore_above" : 256
        },
        "pinyin" : {
          "type" : "text",
          "analyzer" : "pinyin"
        }
      }
    },
    "type" : {
      "type" : "long"
    }
  }
}
  
#URI查询
GET /security-evaluation-v2/_search?q=title:标题

#带profile的查询
GET /security-evaluation-v2/_search?q=标题5&df=title
{
  "profile": "true"
}

#泛查询，正对_all,所有字段，性能不佳
GET /security-evaluation-v2/_search?q=标题
{
  "profile": "true"
}

#使用双引号，Phrase查询，双引号包含的内容出现的顺序必须一致和必须同时出现
#"标题 2"，等效于 标题 AND 2 。Phrase查询，还要求前后顺序保持一致
GET /security-evaluation-v2/_search?q=title:"标题 2"
{
  "profile": "true"
}

#不加双引号的 标题 2 相当于 标题 OR 2
GET /security-evaluation-v2/_search?q=title:标题 2
{
  "profile": "true"
}

# 分组，Bool查询 (标题 AND 2) (标题 2)=(标题 OR 2) (标题 NOT 2)表示必须包括标题但不包括2
GET /security-evaluation-v2/_search?q=title:(标题 2)
{
  "profile": "true"
}
#URI还可以使用通配符*，?和正则表达式的查询语法

#-------------------------------------------------------------------------------------#

#Request Body查询 分页 排序
GET /security-evaluation-v2/_search
{
  "from": 0,
  "size": 20,
  "sort": [
    {
      "id": {
        "order": "desc"
      }
    }
  ], 
  "query": {
    "match_all": {}
  }
}

#指定_source返回需要的字段,_source支持通配符
POST /security-evaluation-v2/_search
{
  "query": {
    "match_all": {}
  }, 
  "_source": ["id","title"]
}

# 脚本字段
GET /security-evaluation-v2/_search
{
  "script_fields": {
    "new_name": {
      "script": {
        "lang": "painless",
        "source": "doc['type']"
      }
    }
  }, 
  "query": {
    "match_all": {}
  }
}

# Request Body中的match，如果是使用 "title":"标签2" 默认是标题和2的OR的关系，如果想使用AND的关系，需要添加operator操作符
GET /security-evaluation-v2/_search
{
  "query": {
    "match": {
      "title": {
        "query": "标题2",
        "operator": "or"
      },
      "name": {
        "query": "刘八"
      }
    }
  }
}

# 对字段查询
GET /security-evaluation-v2/_search
{
  "query": {
    "multi_match": {
      "query": "标题2",
      "fields": ["title","name","content","title.pinyin","name.pinyin"]
    }
  },
  "profile": "true"
}

# 多字段查询
# type是固定字段，查询type=1的(使用filter)，多字段查询，查询需要查询的字段
GET /security-evaluation-v2/_search
{
  "query": {
    "bool": {
      "filter": {
        "term": {
          "type": "1"
        }
      }, 
      "must": [
        {
          "multi_match": {
            "query": "标题2",
            "fields": ["title","name","content","title.pinyin","name.pinyin"]
          }
        }
      ]
    }
  },
  "profile": "true"
}


# 多字段查询
# type是固定字段，查询type=1的(使用match)和filter区别在于filter不参与算分性能更好，多字段查询，查询需要查询的字段
GET /security-evaluation-v2/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "type": "1"
          }
        },
        {
          "multi_match": {
            "query": "标题2",
            "fields": ["title","name","content","title.pinyin","name.pinyin"]
          }
        }
      ]
    }
  },
  "profile": "true"
}

# 使用match phrase 默认情况跟上面使用了operator+and效果相同
GET /security-evaluation-v2/_search
{
  "query": {
    "match_phrase": {
      "title": {
        "query": "标题2"
      }
    }
  }
}

#------DisjunctionMaxQuery 查询-------
GET /security-evaluation-v2/_search
{
  "profile": "true", 
  "query": {
    "dis_max": {
      //1.获得最佳匹配语句的评分_score。
      //2.将其他匹配语句的评分与tie_breaker相乘。
      //3.对以上评分求和并规范化
      "tie_breaker": 0.7,
      "boost": 1.2,
      "queries": [
        {
          "match": {
            "title": "标题2"
          }
        },
        {
          "match": {
            "name": "张三2"
          }
        }
      ]
    }
  }
}

#-----------------------------------Query String和Simple Query String-----------------------------------

# "query":"标题 AND 2"、"query":"标题 NOT 2"或者"query":"标题 OR 2"，也可以使用()进行分组
GET /security-evaluation-v2/_search
{
  "query": {
    "query_string": {
      "default_field": "title",
      "query": "标题 OR 2"
    }
  }
}
GET /security-evaluation-v2/_search
{
  "query": {
    "query_string": {
      "fields": ["name","title"],
      "query": "(标题 OR 2) AND (张三 AND 2)"
    }
  }
}

#-----------------------------------index template和Dynamic Tempate-----------------------------------

# 查看index template
GET _template

GET _template/security-evaluation-template-v1

# 创建index 模板 index_patterns:security-evaluation-*表示所有使用security-evaluation-开头的都会应用该模板
PUT _template/security-evaluation-template-v1
{
  "index_patterns": "security-evaluation-*",
  "settings" : {
    "analysis": {
      "analyzer": {
        
        "hanlp": {
          "tokenizer": "hanlp_standard"
        },
        "pinyin": {
          "tokenizer": "pinyin"
        }
      }
    }
  },
  "mappings" : {
    "properties" : {
      "createTime" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "fileId" : {
        "type" : "keyword",
        "index" : false
      },
      "id" : {
        "type" : "long"
      },
      "name" : {
        "type" : "text",
        "analyzer": "hanlp",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          },
          "pinyin" : {
            "type" : "text",
            "analyzer" : "pinyin"
          }
        }
      },
      "content" : {
        "type" : "text",
        "analyzer": "hanlp",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "status" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "title" : {
        "type" : "text",
        "analyzer": "hanlp",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          },
          "pinyin" : {
            "type" : "text",
            "analyzer" : "pinyin"
          }
        }
      },
      "type" : {
        "type" : "long"
      }
    }
  }
}

# Dynamic Tempate是写在具体的index的mapping中的
PUT _template/security-evaluation-template-v1
{
  "index_patterns": "security-evaluation-*",
  "settings" : {
    "analysis": {
      "analyzer": {
        
        "hanlp": {
          "tokenizer": "hanlp_standard"
        },
        "pinyin": {
          "tokenizer": "pinyin"
        }
      }
    }
  },
  "mappings" : {
    "dynamic_templates" : [
      {
        "strings_as_keywords" : {
          "match" : "*",
          "mapping" : {
            "type" : "keyword"
          }
        }
      }
    ],
    "properties" : {
      "createTime" : {
        "type" : "text",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      }
    }
  }
}

#---------------------------ES的统计--------------------------------、
#Bucket 根据类型统计 可以在aggs中嵌套aggs来统计Metrics
GET /security-evaluation-v2/_search
{
  "size": 0,
  "aggs": {
    "types": {
      "terms": {
        "field": "type"
      }
    }
  }
}

#----------------------结构化查询----------------------------
#使用term进行结构化查询
GET /security-evaluation-v2/_search
{
  "profile": "true", 
  //查看算分情况
  "explain": true, 
  "query": {
    "term": {
      "id": {
        "value": "1579508663528"
      }
    }
  }
}

#使用constant_score+filter不让ES进行算分，提高性能
GET /security-evaluation-v2/_search
{
  "profile": "true", 
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "id": "1579508663528"
        }
      },
      //指定分数
      "boost": 1.0
    }
  }
}

#--------创建Search Template-----------------
POST _scripts/flights_search_temp
{
  "script" : {
    "lang": "mustache",
    "source": {
      "query": {
        "match" : {
          "DestCountry": "{{query_string}}"
        }
      }
    }
  }
}

# 查看模板
GET _scripts/flights_search_temp

# 使用模板
GET _search/template
{
  "id":"flights_search_temp",
  "params": {
    "query_string":"CN"
  }
}

#--------------自动补全------------------------
PUT articles
{
  "mappings": {
    "properties": {
      "title_completion": {
        //创建completion类型
        "type": "completion"
      }
    }
  }
}

GET /articles

POST /articles/_bulk
{ "index": {}}
{ "title_completion":"lucene is very cool" }
{ "index": {}}
{ "title_completion":"Elasticsearch builds on top of lucene" }
{ "index": {}}
{ "title_completion":"Elasticsearch rocks" }
{ "index": {}}
{ "title_completion":"elastic is the company behind ELK stack" }
{ "index": {}}
{ "title_completion":"Elk stack rocks" }
{ "index": {}}

# 自动补全查询
POST /articles/_search
{
  "size": 0,
  "suggest": {
    "article-suggester": {
      "prefix":"elk",
      "completion": {
        "field": "title_completion"
      }
    }
  }
}
```
## bool查询
![./boo查询](./img/bool查询.png)

## 数字Range
![数字Range](./img/shuzi.png)

## 日期Range
![日期Range](./img/riqi.png)

## Multi Match三种使用场景
![Multi Match三种使用场景](./img/multi-match.png)
### 三种情况的使用
```json
POST /security-evaluation-v2/_search
{
  "query": {
    "multi_match": {
      "type": "best_fields",
      "query": "Quick pets",
      "fields": ["title","body"],
      "tie_breaker": 0.2,
      "minimum_should_match": "20%"
    }
  }
}
```
## 集群节点的职责
![节点参数配置](./img/节点参数配置.png)
![单一职责节点](./img/单一职责节点.png)
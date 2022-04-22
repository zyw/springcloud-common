## 1. ignore关键字
```sql
INSERT IGNORE INTO `sys_dict_type` (`id`, `name`, `status`, `create_time`, `update_time`)
VALUES
	(34, '知识库分类1', 1, '2020-02-28 00:06:03', '2020-02-28 00:06:03');
```
上面的`sql`的`insert`语句中包含`ignore`语句，执行结果是，如果主键存在就忽略，如果不存在就新增。对应上面的语句就是`34`这个主键。

## 2. ON DUPLICATE KEY UPDATE
```sql
INSERT IGNORE INTO `sys_dict_type` (`id`, `name`, `status`, `create_time`, `update_time`)
VALUES
	(34, '知识库分类1', 1, '2020-02-28 00:06:03', '2020-02-28 00:06:03') ON DUPLICATE KEY UPDATE `name`='知识库分类2';
```
上面的语句是如果主键`34`不存在就新增一条记录，如果`34`存在就把`name`修改成【知识库分类2】。

## 3. REPLACE
```sql
REPLACE INTO users (id,name,age) VALUES(123, '贾斯丁比伯', 22); 
```
使用`REPLACE`的最大好处就是可以将`DELETE`和`INSERT`合二为一，形成一个原子操作。这样就可以不必考虑在同时使用`DELETE`和`INSERT`时添加事务等复杂操作了。
在使用`REPLACE`时，表中必须有唯一索引，而且这个索引所在的字段不能允许空值，否则`REPLACE`就和`INSERT`完全一样的。在执行`REPLACE`后，系统返回了所影响的行数，如果返回`1`，
说明在表中并没有重复的记录，如果返回`2`，说明有一条重复记录，系统自动先调用了`DELETE`删除这条记录，然后再记录用`INSERT`来插入这条记录。

## 参考
[MySql_插入记录时,存在就更新（或不做任何动作），不存在就添加](https://blog.csdn.net/BuptZhengChaoJie/article/details/50992923)
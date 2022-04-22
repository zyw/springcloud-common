/*
SQLyog Ultimate v11.5 (64 bit)
MySQL - 5.7.13-log : Database - v5platform
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`v5platform` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `v5platform`;

/*Table structure for table `databasechangelog` */

DROP TABLE IF EXISTS `databasechangelog`;

CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `databasechangelog` */

insert  into `databasechangelog`(`ID`,`AUTHOR`,`FILENAME`,`DATEEXECUTED`,`ORDEREXECUTED`,`EXECTYPE`,`MD5SUM`,`DESCRIPTION`,`COMMENTS`,`TAG`,`LIQUIBASE`,`CONTEXTS`,`LABELS`,`DEPLOYMENT_ID`) values ('init-database-res','zyw','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:25',1,'EXECUTED','7:865301ef59794abe4feb623c027c1bdb','sql','',NULL,'3.5.3',NULL,NULL,'8083185056'),('init-database-role','zyw','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:26',2,'EXECUTED','7:bc822db6a4f224ada75445831a65a1e9','sql','',NULL,'3.5.3',NULL,NULL,'8083185056'),('init-database-user','zyw','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:26',3,'EXECUTED','7:1250b5d8bbad879e71e30606bc3afc19','sql','',NULL,'3.5.3',NULL,NULL,'8083185056'),('init-database-user-login_name-key','','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:26',4,'EXECUTED','7:b07f4f6d0e1fbdd1acf9b7fd6891cbfb','sql','',NULL,'3.5.3',NULL,NULL,'8083185056'),('init-database-user-role-res','zyw','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:27',5,'EXECUTED','7:7c58c6283a2aef194a2fc1af300904e3','sql','',NULL,'3.5.3',NULL,NULL,'8083185056'),('init-database-table-sys_dic-and-sys_dict_type','zyw','classpath:migrates/init/db.changelog.xml','2018-02-08 17:46:28',6,'EXECUTED','7:ec375b7dcdf79f85577420b397219115','sql','',NULL,'3.5.3',NULL,NULL,'8083185056');

/*Table structure for table `databasechangeloglock` */

DROP TABLE IF EXISTS `databasechangeloglock`;

CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `databasechangeloglock` */

insert  into `databasechangeloglock`(`ID`,`LOCKED`,`LOCKGRANTED`,`LOCKEDBY`) values (1,'\0',NULL,NULL);

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL,
  `resource_ids` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  `tenant_id` varchar(36) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `create_by` varchar(36) DEFAULT NULL,
  `update_by` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`,`tenant_id`,`status`,`purpose`,`create_time`,`update_time`,`create_by`,`update_by`) values ('frontend',NULL,'$e0801$65x9sjjnRPuKmqaFn3mICtPYnSWrjE7OB/pKzKTAI4ryhmVoa04cus+9sJcSAFKXZaJ8lcPO1I9H22TZk6EN4A==$o+ZWccaWXSA2t7TxE5VBRvz2W8psujU3RPPvejvNs4U=','all','password,refresh_token,authorization_code','http://localhost:8080',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `sys_client_details` */

DROP TABLE IF EXISTS `sys_client_details`;

CREATE TABLE `sys_client_details` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
  `client_id` varchar(200) DEFAULT NULL COMMENT 'client id`',
  `client_secret` varchar(200) DEFAULT NULL COMMENT 'secret',
  `resource_ids` varchar(200) DEFAULT NULL COMMENT 'resourceid逗号(,)分割',
  `scope` varchar(200) DEFAULT NULL COMMENT 'scope',
  `authorized_grant_types` varchar(200) DEFAULT NULL COMMENT 'authorized_grant_types逗号(,)分割',
  `web_server_redirect_uri` varchar(200) DEFAULT NULL COMMENT 'redirect uri',
  `authorities` varchar(200) DEFAULT NULL COMMENT '权限逗号(,)分割',
  `access_token_validity` varchar(200) DEFAULT NULL,
  `refresh_token_validity` varchar(200) DEFAULT NULL,
  `additional_information` varchar(200) DEFAULT NULL,
  `autoapprove` varchar(200) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态，1 未删除，0 禁用，-1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置OAuth2的客户端相关信息表';

/*Data for the table `sys_client_details` */

insert  into `sys_client_details`(`id`,`client_id`,`client_secret`,`resource_ids`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`,`create_time`,`update_time`,`status`) values (1,'webapp',NULL,NULL,'nsop','password,authorization_code,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL,'2018-06-04 09:55:51','2018-06-04 09:55:54',1),(2,'mobile',NULL,NULL,'nsop','password,authorization_code,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL,'2018-06-04 09:55:56','2018-06-04 09:55:59',1),(3,'verification',NULL,NULL,'nsop','password,authorization_code,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL,'2018-06-04 09:56:01','2018-06-04 09:56:03',1),(4,'traffic001','123456789',NULL,'pay','client_credentials,refresh_token',NULL,NULL,NULL,NULL,NULL,NULL,'2018-06-04 09:56:05','2018-06-04 09:56:07',1),(5,'frontend','$e0801$65x9sjjnRPuKmqaFn3mICtPYnSWrjE7OB/pKzKTAI4ryhmVoa04cus+9sJcSAFKXZaJ8lcPO1I9H22TZk6EN4A==$o+ZWccaWXSA2t7TxE5VBRvz2W8psujU3RPPvejvNs4U=',NULL,'all','password,refresh_token,authorization_code',NULL,NULL,NULL,NULL,NULL,NULL,'2018-10-16 15:47:41','2018-10-16 15:50:35',1),(6,'gateway','$e0801$2pCoZNfHsARuc1+5Cg8uPeq/PakWIECLOwa+/9iZFL+CXfn/0HYV3Qav00szvOIVaeRZkpjMkUoqwj49DtoDbg==$xwWDp9rOh1tPzlSHopOM5OgWx264H6gY0DACx9AglAQ=',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2018-10-18 16:18:23','2018-10-18 16:18:23',1);

/*Table structure for table `sys_dict` */

DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type_id` varchar(20) NOT NULL COMMENT '类型ID',
  `sort_num` int(5) DEFAULT '1' COMMENT '排序',
  `name` varchar(20) NOT NULL COMMENT '字典名称',
  `value` varchar(20) DEFAULT '1' COMMENT '字典值',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1:正常，0：非正常',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

/*Data for the table `sys_dict` */

/*Table structure for table `sys_dict_type` */

DROP TABLE IF EXISTS `sys_dict_type`;

CREATE TABLE `sys_dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '字典类型名称',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1:正常，0：非正常',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典类型表';

/*Data for the table `sys_dict_type` */

/*Table structure for table `sys_res` */

DROP TABLE IF EXISTS `sys_res`;

CREATE TABLE `sys_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) DEFAULT NULL,
  `name` varchar(111) DEFAULT NULL,
  `permission` varchar(100) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sort_num` int(11) DEFAULT '1' COMMENT '排序',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图片',
  `pids` varchar(100) DEFAULT NULL COMMENT 'TreeTable排序',
  `type` int(1) DEFAULT '2' COMMENT '1 菜单 2 按钮',
  `des` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '1 可用 0 不可用 -1: 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission_wy` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_res` */

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(55) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `sort_num` int(11) DEFAULT '1',
  `des` varchar(55) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT '1 可用 0 不可用 -1: 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_wy` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role` */

/*Table structure for table `sys_role_res` */

DROP TABLE IF EXISTS `sys_role_res`;

CREATE TABLE `sys_role_res` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `res_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SYS_ROLE_RES_RES_ID` (`res_id`),
  KEY `FK_SYS_ROLE_RES_ROLE_ID` (`role_id`),
  CONSTRAINT `FK_SYS_ROLE_RES_RES_ID` FOREIGN KEY (`res_id`) REFERENCES `sys_res` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_SYS_ROLE_RES_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_res` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(100) NOT NULL,
  `pwd` varchar(500) NOT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `sex` int(1) DEFAULT '1' COMMENT '1:男，0:女',
  `telephone` varchar(11) DEFAULT NULL COMMENT '电话',
  `login_count` int(10) DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `status` int(11) DEFAULT '1' COMMENT '1 可用 0 不可用 -1: 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_unique` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`login_name`,`pwd`,`salt`,`name`,`email`,`sex`,`telephone`,`login_count`,`last_login_time`,`avatar`,`status`,`create_time`,`update_time`) values (1000,'zhangsan','$e0801$K0y4S8FtuZpYnkXnBtxvm94p+VUcjwa32GjKTTylxa7Yss6SbuNbjrwBgBk0I4MOuGTpfNTY4FBZ+iTuiRqGtg==$1+o17JB7yYCRCTZaP2+X3BQJ2hHXSe2N7k22Yw6jlNI=',NULL,'张三','zhangsan@163.com',1,'13691918047',0,'2018-02-09 14:39:46',NULL,1,'2018-02-09 14:39:50','2018-02-09 14:39:53');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_SYS_USER_ROLE_USER_ID` (`user_id`),
  KEY `FK_SYS_USER_ROLE_ROLE_ID` (`role_id`),
  CONSTRAINT `FK_SYS_USER_ROLE_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_SYS_USER_ROLE_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_role` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

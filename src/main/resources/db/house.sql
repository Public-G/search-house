/*
SQLyog Trial v13.0.0 (64 bit)
MySQL - 8.0.11 : Database - house
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`house` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `house`;

/*Table structure for table `schedule_job` */

DROP TABLE IF EXISTS `schedule_job`;

CREATE TABLE `schedule_job` (
  `job_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(256) NOT NULL COMMENT 'spring bean名称',
  `method_name` varchar(128) NOT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(128) NOT NULL COMMENT 'cron表达式',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `schedule_job` */

insert  into `schedule_job`(`job_id`,`bean_name`,`method_name`,`params`,`cron_expression`,`status`,`remark`,`create_time`,`update_time`) values 
(1,'testTask','test','search-house','0 0 18 8 * ?',1,'测试带参数的','2018-11-09 17:14:18',NULL),
(2,'testTask','test2','','0 52 12 10 * ?',0,'测试不带参数的','2018-11-09 17:16:00',NULL),
(3,'testTask','test','test-params','0 0 18 9 * ?',1,'每个月9号18点0分0秒执行带参数的test方法','2018-11-10 00:48:57',NULL),
(12,'snycHouseTask','syncHouse','','0 0 3 1 * ?',0,'每个月1号凌晨3点同步Elasticsearch和LBS的房源数据','2019-01-01 18:18:52','2019-01-01 21:14:28');

/*Table structure for table `schedule_job_log` */

DROP TABLE IF EXISTS `schedule_job_log`;

CREATE TABLE `schedule_job_log` (
  `log_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) unsigned NOT NULL COMMENT '任务id',
  `bean_name` varchar(256) NOT NULL COMMENT 'spring bean名称',
  `method_name` varchar(128) NOT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int(11) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime NOT NULL COMMENT '执行时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_job_id` (`job_id`) COMMENT '任务id索引'
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `schedule_job_log` */

insert  into `schedule_job_log`(`log_id`,`job_id`,`bean_name`,`method_name`,`params`,`status`,`error`,`times`,`create_time`) values 
(1,1,'testTask','test','search-house',0,NULL,13550,'2018-11-09 20:32:57'),
(2,2,'testTask','test',NULL,1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test()',13,'2018-11-09 20:33:10'),
(3,1,'testTask','test','search-house',0,NULL,9209,'2018-11-09 20:33:01'),
(4,2,'testTask','test',NULL,1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test()',2,'2018-11-09 20:33:15'),
(5,1,'testTask','test','search-house',0,NULL,1103,'2018-11-09 20:33:49'),
(6,1,'testTask','test','search-house',0,NULL,1008,'2018-11-09 20:33:50'),
(7,2,'testTask','test',NULL,1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test()',3,'2018-11-09 20:34:07'),
(8,2,'testTask','test',NULL,1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test()',2,'2018-11-09 20:34:28'),
(9,2,'testTask','test',NULL,1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test()',1,'2018-11-09 20:34:28'),
(10,1,'testTask','test','search-house',0,NULL,9506,'2018-11-09 20:37:40'),
(11,1,'testTask','test','search-house',0,NULL,1164,'2018-11-09 20:37:48'),
(12,1,'testTask','test','search-house',0,NULL,1407,'2018-11-09 20:37:49'),
(13,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 20:37:51'),
(14,2,'testTask','test2',NULL,0,NULL,3,'2018-11-09 20:38:02'),
(15,1,'testTask','test','search-house',0,NULL,1035,'2018-11-09 20:38:01'),
(16,2,'testTask','test2',NULL,0,NULL,16,'2018-11-09 20:38:03'),
(17,2,'testTask','test2',NULL,0,NULL,4,'2018-11-09 20:38:04'),
(18,2,'testTask','test2',NULL,0,NULL,11,'2018-11-09 20:38:08'),
(19,1,'testTask','test','search-house',0,NULL,3503,'2018-11-09 20:41:50'),
(20,2,'testTask','test2',NULL,0,NULL,3,'2018-11-09 20:59:01'),
(21,2,'testTask','test2',NULL,0,NULL,2,'2018-11-09 20:59:02'),
(22,2,'testTask','test2',NULL,0,NULL,18,'2018-11-09 20:59:03'),
(23,2,'testTask','test2',NULL,0,NULL,14,'2018-11-09 20:59:04'),
(24,1,'testTask','test','search-house',0,NULL,1057,'2018-11-09 20:59:05'),
(25,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 20:59:10'),
(26,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 20:59:15'),
(27,1,'testTask','test','search-house',0,NULL,1024,'2018-11-09 20:59:20'),
(28,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 20:59:25'),
(29,1,'testTask','test','search-house',0,NULL,1022,'2018-11-09 20:59:30'),
(30,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 20:59:35'),
(31,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 20:59:40'),
(32,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 20:59:45'),
(33,1,'testTask','test','search-house',0,NULL,1005,'2018-11-09 20:59:50'),
(34,1,'testTask','test','search-house',0,NULL,1011,'2018-11-09 20:59:55'),
(35,2,'testTask','test2',NULL,0,NULL,2,'2018-11-09 21:00:01'),
(36,2,'testTask','test2',NULL,0,NULL,2,'2018-11-09 21:00:02'),
(37,2,'testTask','test2',NULL,0,NULL,13,'2018-11-09 21:00:03'),
(38,2,'testTask','test2',NULL,0,NULL,12,'2018-11-09 21:00:04'),
(39,1,'testTask','test','search-house',0,NULL,1005,'2018-11-09 21:00:05'),
(40,1,'testTask','test','search-house',0,NULL,1005,'2018-11-09 21:00:10'),
(41,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 21:00:15'),
(42,1,'testTask','test','search-house',0,NULL,1012,'2018-11-09 21:00:20'),
(43,1,'testTask','test','search-house',0,NULL,1008,'2018-11-09 21:00:25'),
(44,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 21:00:30'),
(45,1,'testTask','test','search-house',0,NULL,1018,'2018-11-09 21:00:35'),
(46,1,'testTask','test','search-house',0,NULL,1005,'2018-11-09 21:00:40'),
(47,1,'testTask','test','search-house',0,NULL,1017,'2018-11-09 21:00:45'),
(48,1,'testTask','test','search-house',0,NULL,1009,'2018-11-09 21:00:50'),
(49,1,'testTask','test','search-house',0,NULL,1009,'2018-11-09 21:00:55'),
(50,2,'testTask','test2',NULL,0,NULL,7,'2018-11-09 21:01:01'),
(51,2,'testTask','test2',NULL,0,NULL,20,'2018-11-09 21:01:02'),
(52,2,'testTask','test2',NULL,0,NULL,5,'2018-11-09 21:01:03'),
(53,2,'testTask','test2',NULL,0,NULL,3,'2018-11-09 21:01:04'),
(54,1,'testTask','test','search-house',0,NULL,1003,'2018-11-09 21:01:05'),
(55,1,'testTask','test','search-house',0,NULL,1013,'2018-11-09 21:01:10'),
(56,1,'testTask','test','search-house',0,NULL,1027,'2018-11-09 21:01:15'),
(57,1,'testTask','test','search-house',0,NULL,1012,'2018-11-09 21:01:20'),
(58,1,'testTask','test','search-house',0,NULL,1005,'2018-11-09 21:01:25'),
(59,1,'testTask','test','search-house',0,NULL,1017,'2018-11-09 21:01:30'),
(60,1,'testTask','test','search-house',0,NULL,1004,'2018-11-09 21:01:35'),
(61,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 21:01:40'),
(62,1,'testTask','test','search-house',0,NULL,1012,'2018-11-09 21:01:45'),
(63,1,'testTask','test','search-house',0,NULL,1019,'2018-11-09 21:01:50'),
(64,1,'testTask','test','search-house',0,NULL,1007,'2018-11-09 21:01:55'),
(65,2,'testTask','test2',NULL,0,NULL,4,'2018-11-09 21:02:01'),
(66,2,'testTask','test2',NULL,0,NULL,6,'2018-11-09 21:02:02'),
(67,2,'testTask','test2',NULL,0,NULL,6,'2018-11-09 21:02:03'),
(68,2,'testTask','test2',NULL,0,NULL,21,'2018-11-09 21:02:04'),
(69,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 21:02:05'),
(70,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 21:02:10'),
(71,1,'testTask','test','search-house',0,NULL,1017,'2018-11-09 21:02:15'),
(72,1,'testTask','test','search-house',0,NULL,1009,'2018-11-09 21:02:20'),
(73,1,'testTask','test','search-house',0,NULL,1011,'2018-11-09 21:02:25'),
(74,1,'testTask','test','search-house',0,NULL,1008,'2018-11-09 21:02:30'),
(75,1,'testTask','test','search-house',0,NULL,1006,'2018-11-09 21:02:35'),
(76,1,'testTask','test','search-house',0,NULL,1009,'2018-11-09 21:02:40'),
(77,1,'testTask','test','search-house',0,NULL,1012,'2018-11-09 21:02:45'),
(78,1,'testTask','test','search-house',0,NULL,1037,'2018-11-10 12:31:10'),
(79,10,'1','1','1',1,'com.github.common.exception.SHException: 没有找到bean组件',6,'2018-11-10 12:44:26'),
(80,10,'1','1','1',1,'com.github.common.exception.SHException: 没有找到bean组件',0,'2018-11-10 12:46:25'),
(81,10,'1','1','1',1,'org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named \'1\' available',4,'2018-11-10 12:47:34'),
(82,3,'testTask','test1','test-params',1,'java.lang.NoSuchMethodException: com.github.modules.data.task.TestTask.test1(java.lang.String)',8,'2018-11-10 12:48:34'),
(83,3,'testTask','test','test-params',0,NULL,1038,'2018-11-10 12:49:22'),
(84,2,'testTask','test2','',0,NULL,2,'2018-11-10 12:52:00'),
(85,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',499,'2019-01-01 18:20:00'),
(86,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',61192,'2019-01-01 18:28:00'),
(87,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',259132,'2019-01-01 18:32:00'),
(88,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',41915,'2019-01-01 18:53:00'),
(89,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException',83108,'2019-01-01 19:02:00'),
(90,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException',31093,'2019-01-01 19:10:00'),
(91,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException',41192,'2019-01-01 19:13:00'),
(92,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException',114267,'2019-01-01 19:16:00'),
(93,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',10268,'2019-01-01 19:46:02'),
(94,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',12752,'2019-01-01 20:39:00'),
(95,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',168207,'2019-01-01 20:48:00'),
(96,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',38277,'2019-01-01 21:00:07'),
(97,12,'snycHouseTask','syncHouse','',1,'java.util.concurrent.ExecutionException: com.github.common.exception.SHException: 执行定时任务失败',123496,'2019-01-01 21:02:00'),
(98,12,'snycHouseTask','syncHouse','',0,NULL,124950,'2019-01-01 21:10:00');

/*Table structure for table `sys_log` */

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `log_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(64) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(256) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) unsigned NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_log` */

insert  into `sys_log`(`log_id`,`username`,`operation`,`method`,`params`,`time`,`ip`,`create_time`) values 
(1,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":5,\"roleName\":\"1\",\"remark\":\"1\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546627490687}]',57,'0:0:0:0:0:0:0:1','2019-01-05 02:44:51'),
(2,'admin','删除角色','com.github.modules.sys.controller.SysRoleController.delete()','[[5]]',38,'0:0:0:0:0:0:0:1','2019-01-05 02:46:59'),
(3,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":6,\"roleName\":\"1\",\"remark\":\"1\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629737744}]',23,'0:0:0:0:0:0:0:1','2019-01-05 03:22:18'),
(4,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":7,\"roleName\":\"2\",\"remark\":\"2\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629766489}]',71,'0:0:0:0:0:0:0:1','2019-01-05 03:22:47'),
(5,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":8,\"roleName\":\"3\",\"remark\":\"3\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629787707}]',50,'0:0:0:0:0:0:0:1','2019-01-05 03:23:08'),
(6,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":9,\"roleName\":\"5\",\"remark\":\"5\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629835132}]',44,'0:0:0:0:0:0:0:1','2019-01-05 03:23:55'),
(7,'admin','删除角色','com.github.modules.sys.controller.SysRoleController.delete()','[[9,8,7,6]]',47,'0:0:0:0:0:0:0:1','2019-01-05 03:24:00'),
(8,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":10,\"roleName\":\"1\",\"remark\":\"1\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629862572}]',25,'0:0:0:0:0:0:0:1','2019-01-05 03:24:23'),
(9,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":11,\"roleName\":\"23\",\"remark\":\"3\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629889128}]',27,'0:0:0:0:0:0:0:1','2019-01-05 03:24:49'),
(10,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":12,\"roleName\":\"3\",\"remark\":\"4\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546629912994}]',58,'0:0:0:0:0:0:0:1','2019-01-05 03:25:13'),
(11,'admin','删除角色','com.github.modules.sys.controller.SysRoleController.delete()','[[12,11,10]]',28,'0:0:0:0:0:0:0:1','2019-01-05 03:25:22'),
(12,'admin','保存角色','com.github.modules.sys.controller.SysRoleController.save()','[{\"roleId\":13,\"roleName\":\"1\",\"remark\":\"1\",\"createUserId\":1,\"createUserName\":null,\"createTime\":1546630208296}]',30,'0:0:0:0:0:0:0:1','2019-01-05 03:30:08'),
(13,'admin','删除角色','com.github.modules.sys.controller.SysRoleController.delete()','[[13]]',22,'0:0:0:0:0:0:0:1','2019-01-05 03:33:19');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父菜单ID，顶级菜单为0',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `request_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单图标',
  `type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '类型   0：顶级目录   1：目录，1：目录  2：菜单  3：按钮',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`parent_id`,`menu_name`,`request_url`,`perms`,`icon`,`type`,`order_num`) values 
(1,0,'数据管理',NULL,NULL,'database',0,3),
(2,0,'用户中心',NULL,NULL,'users',0,2),
(3,0,'系统管理',NULL,NULL,'cogs',0,1),
(4,3,'管理员列表','/sys/user/forward/List','sys:user:forward:userList','user',2,9),
(6,2,'用户管理','/sys/front/user/forward/List',NULL,'user-o',2,NULL),
(9,4,'新增',NULL,'sys:user:forward:userAdd',NULL,3,4),
(11,1,'房源管理','/data/house/forward/List',NULL,'home',2,9),
(12,1,'定时任务','/data/schedule/forward/List',NULL,'clock-o',2,8),
(13,1,'规则管理','/data/rule/forward/List','','leaf',2,7),
(14,1,'项目管理','/data/spider/forward/List',NULL,'bug',2,5),
(15,1,'参数设置','/data/setting/forward/List',NULL,'newspaper-o',2,6),
(16,3,'菜单管理','/sys/menu/forward/List',NULL,'bars',2,7),
(21,3,'角色管理','/sys/role/forward/List','','user-secret',2,8),
(22,3,'系统日志','/sys/log/forward/List','','file-text-o',2,6);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) NOT NULL COMMENT '角色名称',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) unsigned DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`) COMMENT '角色名索引'
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role` */

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) unsigned DEFAULT NULL,
  `menu_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`) COMMENT '角色ID索引',
  KEY `idx_menu_id` (`menu_id`) COMMENT '菜单ID索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`role_id`,`menu_id`) values 
(1,2,1),
(2,2,5);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `mobile` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态  0：正常   1：禁用',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`) COMMENT '用户名索引',
  KEY `index_create_user_id` (`create_user_id`) COMMENT '创建者ID索引'
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`username`,`password`,`email`,`mobile`,`status`,`create_user_id`,`create_time`,`last_login_time`) values 
(1,'admin','$2a$10$aPJoYLQYJ9GMMI/oqqDL5eF.tgyP0USHuZZ2dGH8KIzs6O8kN.kGq','421499769@qq.com','13005978901',0,0,'2018-10-30 15:13:07','2019-01-05 03:47:23'),
(61,'我是数据管理员','$2a$10$U47zb8cR276MV4rOR5HJqeoS61zrluRV90uiNu7E.eS2dH8cQOE16','666@163.com','13005978902',0,1,'2019-01-02 17:31:40',NULL);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) unsigned DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
  KEY `idx_role_id` (`role_id`) COMMENT '角色ID索引'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`) values 
(1,2,2);

/*Table structure for table `tb_collect` */

DROP TABLE IF EXISTS `tb_collect`;

CREATE TABLE `tb_collect` (
  `collect_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  `house_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`collect_id`),
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
  KEY `idx_house_id` (`house_id`) COMMENT '房源ID索引'
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_collect` */

insert  into `tb_collect`(`collect_id`,`user_id`,`house_id`) values 
(22,1,'5cb1b421e68c6dfe82afd9e6f2a90420');

/*Table structure for table `tb_config` */

DROP TABLE IF EXISTS `tb_config`;

CREATE TABLE `tb_config` (
  `config_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置key',
  `config_value` varchar(512) NOT NULL COMMENT '配置value',
  `config_comment` varchar(512) NOT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`config_id`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_config` */

/*Table structure for table `tb_rule` */

DROP TABLE IF EXISTS `tb_rule`;

CREATE TABLE `tb_rule` (
  `rule_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规则名称',
  `allowed_domains` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '允许爬取的域名',
  `crawl_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '爬虫类类名',
  `loop_start` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情页循环入口',
  `detail_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详情页',
  `region` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区域',
  `next_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '下一页',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `community` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '小区',
  `address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详细地址',
  `square` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '面积',
  `price` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '价格',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `img_href` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面图',
  `house_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '户型',
  `rent_way` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '出租方式',
  `website_name` varchar(32) NOT NULL COMMENT '来源网站',
  `is_dynamic` tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否动态数据，0-是；1-否',
  `release_time` varchar(256) DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`),
  UNIQUE KEY `rule_name` (`rule_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_rule` */

insert  into `tb_rule`(`rule_id`,`rule_name`,`allowed_domains`,`crawl_name`,`loop_start`,`detail_url`,`region`,`next_url`,`title`,`community`,`address`,`square`,`price`,`description`,`img_href`,`house_type`,`rent_way`,`website_name`,`is_dynamic`,`release_time`,`create_time`,`update_time`) values 
(13,'房天下规则','fang.com','','//div[contains(@class,\"houseList\")]/dl/dd','./p[1]/a/@href','./p[3]/a[1]/span/text()','//*[@id=\"rentid_D10_01\"]/a[text()=\"下一页\"]/@href','//h1[contains(@class, \"title\")]/text() ','//a[@id=\"agantzfxq_C02_07\"]/text()','//div[contains(@class,\"tr-line\")]/div/div[contains(text(),\"地      址\")]/following-sibling::*[1]/a/text()','//div[contains(@class, \"trl-item1 w132\")]/div[1]/text()','//div[contains(@class, \"trl-item sty1\")]/i[1]/text()','//div[contains(@class, \"fyms_con floatl gray3\")]/text()','//*[@id=\"agantzfxq_C02_05\"]/div[1]/img[1]/@src','//div[contains(@class, \"trl-item1 w182\")]/div[1]/text()','//div[contains(@class, \"trl-item1 w146\")]/div[1]/text()','房天下',1,'//p[contains(@class, \"gray9 fybh-zf\")]/span[2]/text()','2018-11-22 23:20:26','2018-12-30 04:03:37'),
(21,'107间规则','107room.com','','//div[contains(@class, \"allHouseSearch\")]/div[contains(@class, \"houseResult\")]','./div[contains(@class, \"oneHouse setStyle\")]/a[1]/@href','./div[contains(@class, \"oneHouse setStyle\")]/ul[1]/li[1]/a[1]/span[1]/text()','//*[@id=\"kkpager\"]/a[12]/@href','//*[@id=\"content\"]/div[1]/div[1]/table/tbody/tr/td/h1/text()','//div[contains(@class, \"community_top\")]/div[1]/a[1]/text()','//div[contains(@class, \"community_top\")]/div[3]/span[2]/text()','//table[contains(@class, \"allHouseCondition2\")]/tbody[1]/tr[1]/td[2]/text()','//span[contains(@class, \"monthPrice\")]/text()','//li[contains(@class, \"agentWords\")]/h3/text()','','//table[contains(@class, \"allHouseCondition1\")]/tbody[1]/tr[1]/td/text()','//table[contains(@class, \"allHouseCondition3\")]/tbody[1]/tr[1]/td[2]/text()','107间',1,'//table[contains(@class, \"houseDetail1\")]/tbody[1]/tr[2]/td[2]/text()','2018-12-27 15:16:03','2019-01-04 02:32:41'),
(22,'链家规则','lianjia.com','','//div[contains(@class, \"content__list\")]/div','./a[1]/@href','./div[1]/p[contains(@class, \"content__list--item--des\")]/a[1]/text()','//div[contains(@class, \"content__pg\")]/a[contains(@class, \"next\")]/@href','//p[contains(@class, \"content__title\")]/text()','//div[contains(@class, \"map__cur\")]/text()','./p[contains(@class, \"content__list--item--des\")]/a[2]/text()','//p[contains(@class, \"content__article__table\")]/span[3]/text()','//p[contains(@class, \"content__aside--title\")]/span[1]/text()','//p[contains(@class, \"threeline\")]/text()','//*[@id=\"mySwipe\"]/ul/div[1]/img/@src','//p[contains(@class, \"content__article__table\")]/span[2]/text()','//p[contains(@class, \"content__article__table\")]/span[1]/text()','链家',0,'//div[contains(@class, \"content__subtitle\")]/text()','2018-12-27 15:43:36','2019-01-04 02:52:54'),
(23,'58同城规则','58.com','','//ul[contains(@class, \"listUl\")]/li','./div[1]/a/@href','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[5]/span[2]/a[1]/text()','//*[@id=\"bottom_ad_li\"]//a[contains(@class, \"next\")]/@href','//div[contains(@class, \"house-title\")]/h1/text()','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[4]/span[2]/a[1]/text()','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[6]/span[2]/text()','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[2]/span[2]/text()','//div[contains(@class, \"house-pay-way\")]/span[1]/b[1]/text()','//span[text()=\"房源描述\"]/following-sibling::*[1]/text()','//*[@id=\"smainPic\"]/@src','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[2]/span[2]/text()','//div[contains(@class, \"house-desc-item\")]/ul[contains(@class, \"f14\")]/li[1]/span[2]/text()','58同城',1,'//div[contains(@class, \"house-title\")]/p[contains(@class, \"house-update-info\")]/text()','2019-01-02 04:19:16','2019-01-02 06:29:31'),
(24,'安居客规则','anjuke.com','','//*[@id=\"list-content\"]/div[contains(@class, \"zu-itemmod\")]','./a[1]/@href','//li[contains(@class, \"house-info-item l-width\")]/span[contains(@class, \"type\")]/following-sibling::a[2]/text()','//div[contains(@class, \"multi-page\")]/a[contains(@class, \"aNxt\")]/@href','//h3[contains(@class, \"house-title\")]/text()','//li[contains(@class, \"house-info-item l-width\")]/span[contains(@class, \"type\")]/following-sibling::a[1]/text()','./div[contains(@class, \"zu-info\")]/address[contains(@class, \"details-item\")]/text()','//ul[contains(@class, \"house-info-zufang\")]/li[3]/span[contains(@class, \"info\")]/text()','//ul[contains(@class, \"house-info-zufang\")]//span[contains(@class, \"price\")]/em/text()','//div[contains(@class, \"auto-general\")]//span/text()','//*[@id=\"room_pic_wrap\"]/div[1]/img/@src','//ul[contains(@class, \"house-info-zufang\")]/li[2]/span[contains(@class, \"info\")]/text()','//ul[contains(@class, \"title-label\")]/li[contains(@class, \"rent\")]/text()','安居客',1,'//div[contains(@class, \"right-info\")]/text()','2019-01-03 02:07:57','2019-01-04 13:39:22');

/*Table structure for table `tb_setting` */

DROP TABLE IF EXISTS `tb_setting`;

CREATE TABLE `tb_setting` (
  `setting_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `setting_name` varchar(32) NOT NULL COMMENT '配置名称',
  `cookies_enabled` tinyint(2) unsigned DEFAULT '1' COMMENT '开启cookies, 0：开启,1：禁用',
  `concurrent_requests` int(10) unsigned DEFAULT '16' COMMENT '并发请求数，默认16，最大32',
  `concurrent_requests_per_domain` int(10) unsigned DEFAULT NULL COMMENT '针对每个域名限制n个并发，最大为16个',
  `download_delay` int(10) unsigned DEFAULT NULL COMMENT '最小下载延迟秒数，限制频繁访问，以防止被封号',
  `autothrottle_enabled` tinyint(2) unsigned DEFAULT '1' COMMENT '智能的限速请求，0：开启，1禁用',
  `autothrottle_start_delay` int(10) unsigned DEFAULT NULL COMMENT '初始下载延迟',
  `autothrottle_max_delay` int(10) unsigned DEFAULT NULL COMMENT '在高延迟的情况下设置的最大下载延迟',
  `autothrottle_target_concurrency` float unsigned DEFAULT NULL COMMENT 'Scrapy请求的平均数量应该并行发送每个远程服务器',
  `depth_limit` int(10) unsigned DEFAULT '0' COMMENT '爬虫允许的最大深度，0表示无限制',
  `depth_priority` tinyint(2) unsigned DEFAULT '0' COMMENT '0：深度优先Lifo(默认)，1：广度优先FiFo',
  `httpcache_enabled` tinyint(2) unsigned DEFAULT '1' COMMENT '0：开启, 1：关闭',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`setting_id`),
  UNIQUE KEY `setting_name` (`setting_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_setting` */

insert  into `tb_setting`(`setting_id`,`setting_name`,`cookies_enabled`,`concurrent_requests`,`concurrent_requests_per_domain`,`download_delay`,`autothrottle_enabled`,`autothrottle_start_delay`,`autothrottle_max_delay`,`autothrottle_target_concurrency`,`depth_limit`,`depth_priority`,`httpcache_enabled`,`create_time`,`update_time`) values 
(1,'参数设置v1.0',1,16,16,2,0,1,5,1.5,0,0,1,'2018-11-23 22:24:46','2019-01-04 02:54:41'),
(6,'参数设置v1.1',1,20,16,1,1,1,1,2,100,0,1,'2018-12-06 01:03:12','2019-01-02 04:25:15');

/*Table structure for table `tb_spider` */

DROP TABLE IF EXISTS `tb_spider`;

CREATE TABLE `tb_spider` (
  `spider_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `spider_name` varchar(32) NOT NULL COMMENT '项目名称',
  `start_urls` varchar(256) NOT NULL COMMENT '起始URL',
  `city` varchar(32) NOT NULL COMMENT '城市名',
  `rule_id` bigint(20) unsigned NOT NULL COMMENT '规则ID',
  `setting_id` bigint(20) unsigned DEFAULT NULL COMMENT '参数ID',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`spider_id`),
  UNIQUE KEY `spider_name` (`spider_name`),
  KEY `idx_rule_id` (`rule_id`) COMMENT '规则ID索引',
  KEY `idx_setting_id` (`setting_id`) COMMENT '参数ID索引',
  KEY `idx_city` (`city`) COMMENT '城市名索引'
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_spider` */

insert  into `tb_spider`(`spider_id`,`spider_name`,`start_urls`,`city`,`rule_id`,`setting_id`,`remark`,`create_time`,`update_time`) values 
(12,'房天下上海市项目','http://sh.zu.fang.com/','上海',13,6,'1','2018-11-24 13:34:41','2019-01-02 01:40:18'),
(15,'房天下北京市项目','http://zu.fang.com/','北京',13,6,'2','2018-12-27 13:26:58','2019-01-02 01:51:39'),
(17,'链家北京市项目','https://bj.lianjia.com/zufang/','北京',22,1,'','2018-12-27 15:44:11',NULL),
(18,'链家上海市项目','https://sh.lianjia.com/zufang/','上海',22,6,'','2018-12-27 16:26:47','2018-12-27 16:32:19'),
(19,'58同城北京市项目','https://bj.58.com/zufang/','北京',23,1,'','2019-01-02 04:20:26','2019-01-02 06:08:45'),
(20,'安居客深圳市项目','https://sz.zu.anjuke.com/','深圳',24,1,'','2019-01-03 02:09:00','2019-01-03 02:20:10'),
(21,'107间上海市项目','http://sh.107room.com/z2_a2_1','上海',21,1,'','2019-01-04 02:31:10','2019-01-04 02:31:35'),
(22,'安居客广州市项目','https://gz.zu.anjuke.com/','广州',24,1,'','2019-01-04 13:40:17',NULL);

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`user_id`),
  KEY `idx_mobile` (`mobile`) COMMENT '手机号索引'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user` */

insert  into `tb_user`(`user_id`,`mobile`,`create_time`,`last_login_time`) values 
(1,'13005978901','2018-12-07 14:27:27','2019-01-04 01:24:12');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

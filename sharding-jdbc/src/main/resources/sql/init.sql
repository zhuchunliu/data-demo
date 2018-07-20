DROP DATABASE IF EXISTS  demo_ds_0;
create database demo_ds_0;
USE demo_ds_0;
create table t_order_0(
	order_id BIGINT AUTO_INCREMENT NOT NULL  PRIMARY KEY COMMENT '主键',
	user_id int(32) DEFAULT NULL COMMENT '用户主键',
	type VARCHAR (50)
);
create table t_order_1(
	order_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	user_id int(32) DEFAULT NULL COMMENT '用户主键',
	type VARCHAR (50)
);

create table t_order_item_0(
	order_item_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	order_id BIGINT NOT NULL COMMENT 'order表主键',
	user_id INT NOT NULL
);
create table t_order_item_1(
	order_item_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	order_id BIGINT NOT NULL COMMENT 'order表主键',
	user_id INT NOT NULL
);


DROP DATABASE IF EXISTS  demo_ds_1;
create database demo_ds_1;

USE demo_ds_1;
create table t_order_0(
	order_id BIGINT AUTO_INCREMENT NOT NULL  PRIMARY KEY COMMENT '主键',
	user_id int(32) DEFAULT NULL COMMENT '用户主键',
	type VARCHAR (50)
);
create table t_order_1(
	order_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	user_id int(32) DEFAULT NULL COMMENT '用户主键',
	type VARCHAR (50)
);

create table t_order_item_0(
	order_item_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	order_id BIGINT NOT NULL COMMENT 'order表主键',
	user_id INT NOT NULL
);
create table t_order_item_1(
	order_item_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
	order_id BIGINT NOT NULL COMMENT 'order表主键',
	user_id INT NOT NULL
);

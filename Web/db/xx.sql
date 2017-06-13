drop table user_  CASCADE CONSTRAINTS;
/
drop table category_ CASCADE CONSTRAINTS;
/
drop table item_substance_ CASCADE CONSTRAINTS;
/
drop table substance_ CASCADE CONSTRAINTS;
/
drop table item_ CASCADE CONSTRAINTS;
/
drop table brand_ CASCADE CONSTRAINTS;
/
drop table user_preference_item_ CASCADE CONSTRAINTS;
/
drop table user_counts_ CASCADE CONSTRAINTS;
/
drop table user_comment_item_ CASCADE CONSTRAINTS;
/
drop table category_item_;
/
drop table util;
/
drop table log;
/

create table log (
operation varchar2(30) ,
id varchar2(30) 
)
/

create table util (
id varchar2(30) 
)
/


create table user_ (
id varchar2(30) primary key , 
type varchar2(2) , --0 facebook 1 google 
data_delete date default null,
user_image varchar2(1024),
user_name varchar2(256)
)
/

create table brand_ (
brand_id varchar2(30) primary key ,
name varchar2(60) ,
description clob
)
/

create table category_ (
category_id varchar2(30) primary key ,
name varchar2(60)
)
/

create table substance_ (
id varchar2(30) primary key ,
name varchar2(60) , 
daily_recommended_portion number(5,2)
)
/

create table category_item_ (
item_id varchar2(30) ,
category_id varchar2(30)
)
/


create table item_ (
id varchar2(30) primary key ,
name varchar2(60),
image1 blob,
image2 blob,
image3 blob,
brand_id varchar2(30),
good_tags varchar(1024),
bad_tags varchar(1024),
description clob 
, constraint fk_brand_item foreign key (brand_id) references brand_ (brand_id)
)
/

create table user_comment_item_ (
id varchar2(30) primary key,
user_id varchar2(30) not null,
item_id varchar2(30) not null ,
comm clob,
rating varchar2(4),
good_tags varchar(1024),
bad_tags varchar(1024),
data DATE
)
/

create table item_substance_ (
item_id varchar2(30) not null,
substance_id varchar2(30) not null
, constraint fk_item foreign key (item_id) references item_ (id) 
,  constraint fk_substace foreign key (substance_id) references substance_ (id)
)
/

create table user_preference_item_ (
id varchar2(30) primary key,
item_id varchar2(30) not null ,
user_id varchar2(30) not null,
value number(1) not null 
, constraint fk_item_prefer foreign key (item_id) references item_ (id) 
 , constraint fk_user foreign key (user_id) references user_ (id)
)
/

create table user_counts_ (
user_id varchar2(30) ,
count_likes number(30) , 
count_dislikes number(30) ,
constraint fk_user_count foreign key (user_id) references user_ (id) 
)
/


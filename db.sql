create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table profile (id integer not null, version integer, name varchar(255), primary key (id)) engine=InnoDB
create table user_account (id integer not null, version integer, created_at datetime, last_password_change_at datetime, password varchar(255), username varchar(255), primary key (id)) engine=InnoDB
create table user_account_roles (user_account_id integer not null, roles varchar(255)) engine=InnoDB
alter table user_account add constraint UK_castjbvpeeus0r8lbpehiu0e4 unique (username)
alter table user_account_roles add constraint FKpacca51k3kkqoqs0nbmyugdt2 foreign key (user_account_id) references user_account (id)

# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table mentions (
  id                        bigint auto_increment not null,
  user_id                   bigint not null,
  message_id                bigint not null,
  constraint pk_mentions primary key (id))
;

create table messages (
  id                        bigint auto_increment not null,
  text                      varchar(141) not null,
  author_id                 bigint not null,
  reference_id              bigint,
  date                      datetime not null,
  constraint pk_messages primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  login                     varchar(50) not null,
  password                  varchar(255) not null,
  email                     varchar(255) not null,
  lastname                  varchar(50),
  firstname                 varchar(50),
  description               longtext,
  avatar                    varchar(255),
  register_date             datetime not null,
  constraint uq_users_login unique (login),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;


create table following (
  user_id                        bigint not null,
  friend_id                      bigint not null,
  constraint pk_following primary key (user_id, friend_id))
;
alter table mentions add constraint fk_mentions_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_mentions_user_1 on mentions (user_id);
alter table mentions add constraint fk_mentions_message_2 foreign key (message_id) references messages (id) on delete restrict on update restrict;
create index ix_mentions_message_2 on mentions (message_id);
alter table messages add constraint fk_messages_author_3 foreign key (author_id) references users (id) on delete restrict on update restrict;
create index ix_messages_author_3 on messages (author_id);
alter table messages add constraint fk_messages_reference_4 foreign key (reference_id) references messages (id) on delete restrict on update restrict;
create index ix_messages_reference_4 on messages (reference_id);



alter table following add constraint fk_following_users_01 foreign key (user_id) references users (id) on delete restrict on update restrict;

alter table following add constraint fk_following_users_02 foreign key (friend_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table mentions;

drop table messages;

drop table users;

drop table following;

SET FOREIGN_KEY_CHECKS=1;


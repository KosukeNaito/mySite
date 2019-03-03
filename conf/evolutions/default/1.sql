# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  name                     varchar(255) not null,
  password                      varchar(255) not null,
  constraint uq_account_name unique (name),
  constraint pk_account primary key (name)
);
create sequence account_seq;


# --- !Downs

drop table if exists account;
drop sequence if exists account_seq;

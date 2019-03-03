# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  constraint pk_account primary key (name)
);


# --- !Downs

drop table if exists account;


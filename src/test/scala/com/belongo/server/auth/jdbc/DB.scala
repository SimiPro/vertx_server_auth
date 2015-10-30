package com.belongo.server.auth.jdbc

import scala.collection.mutable

/**
 * Created by Simi on 23.10.2015.
 */
object DB {

  val SQL = mutable.MutableList[String]()
  SQL.+=("drop table if exists user;");
  SQL.+=("drop table if exists user_roles;");
  SQL.+=("drop table if exists roles_perms;");
  SQL.+=("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );");
  SQL.+=("create table user_roles (username varchar(255), role varchar(255));");
  SQL.+=("create table roles_perms (role varchar(255), perm varchar(255));");

  // add user
  SQL.+=("insert into user values('simi', '48FBDF661BBAD5DDF7E57A85DA9D30556519294FD38D75ED5A1035ED2E8BDBDB20C768DEE28740DCAA9715340589AE4BB0AD826DFEB8BE76A5830FA25DC21EF2' , '3483F4A8D46C7D2FDB1DBA1DD9600D0774FF65ED060A0F9A46AC4F6B6A939B7D');")
  SQL.+=("insert into user_roles values ('simi','dev');")
  SQL.+=("insert into user_roles values ('simi','admin');")
  SQL.+=("insert into roles_perms values ('admin','all');")
  SQL.+=("insert into roles_perms values ('dev','commit');")

}

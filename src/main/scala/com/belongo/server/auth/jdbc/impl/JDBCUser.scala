package com.belongo.server.auth.jdbc.impl

import java.lang.Boolean

import io.vertx.core.json.JsonObject
import io.vertx.core.{AsyncResult, Handler}
import io.vertx.ext.auth.{AuthProvider, User}

/**
 * Created by Simi on 23.10.2015.
 */
class JDBCUser(username: Option[String], impl: JDBCAuthImpl, rolePrefix: String) extends User {
  override def principal(): JsonObject = ???

  override def isAuthorised(s: String, handler: Handler[AsyncResult[Boolean]]): User = ???

  override def clearCache(): User = ???

  override def setAuthProvider(authProvider: AuthProvider): Unit = ???
}

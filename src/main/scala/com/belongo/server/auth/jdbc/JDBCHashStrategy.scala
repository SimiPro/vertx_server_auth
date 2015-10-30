package com.belongo.server.auth.jdbc

import io.vertx.core.json.JsonArray

trait JDBCHashStrategy {

  def computeHash(password:String, salt:String):String
  def getHashesStoredPwd(row:JsonArray):String
  def getSalt(row:JsonArray):String

}
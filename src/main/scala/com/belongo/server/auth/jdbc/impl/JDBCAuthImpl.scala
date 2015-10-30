package com.belongo.server.auth.jdbc.impl

import com.belongo.server.auth.jdbc.{JDBCHashStrategy, JDBCAuth}
import com.belongo.server.auth.jdbc.Messages._
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.{Future, AsyncResult, Handler}
import io.vertx.core.json.{JsonArray, JsonObject}
import io.vertx.ext.auth.{ User}
import io.vertx.ext.sql.{ResultSet, SQLConnection}
import io.vertx.rxjava.ext.jdbc.JDBCClient
import io.vertx.rxjava.ext.sql


/**
 * Created by Simi on 23.10.2015.
 */
class JDBCAuthImpl(client: JDBCClient) extends JDBCAuth {
  val logger = LoggerFactory.getLogger(classOf[JDBCAuthImpl])

  val authenticationQuery = DEFAULT_AUTHENTICATE_QUERY
  val authenticateQuery = DEFAULT_AUTHENTICATE_QUERY
  val rolesQuery = DEFAULT_ROLES_QUERY
  val permissionsQuery = DEFAULT_PERMISSIONS_QUERY
  val rolePrefix = DEFAULT_ROLE_PREFIX

  val strategy = new DefaultHashStrategy()


  override def authenticate(authInfo: JsonObject, resultHandler: Handler[AsyncResult[User]]): Unit = {
    val username = Option(authInfo.getString("username"))
    val password = Option(authInfo.getString("password"))

    if (username.isDefined && password.isDefined) {
      executeQuery(authenticationQuery, new JsonArray().add(username.get), resultHandler, (res) => {
        res.getNumRows match {
          case 0 => resultHandler.handle(Future.failedFuture(INVALID_USER_NAME_OR_PASSWORD))
          case 1 => {
            val row = res.getResults.get(0)
            val hashedStoredPwd = strategy.getHashesStoredPwd(row)
            val salt = strategy.getSalt(row)
            val hashedPassword = strategy.computeHash(password.get, salt)
            if (hashedStoredPwd.equals(hashedPassword)) {
              resultHandler.handle(Future.succeededFuture(new JDBCUser(username,this,rolePrefix)))
            } else {
              resultHandler.handle(Future.failedFuture(INVALID_USER_NAME_OR_PASSWORD))
            }
          }
        }
      })

    } else {
      resultHandler.handle(Future.failedFuture(FILL_USERNAME_PASSWORD))
    }




  }

  def executeQuery[T](query:String, params:JsonArray, resultHandler:Handler[AsyncResult[T]], executeOnResult:(ResultSet) => (Unit)): Unit ={
    client.getConnection(new Handler[AsyncResult[sql.SQLConnection]] {
      override def handle(res: AsyncResult[sql.SQLConnection]): Unit = {
        if (res.succeeded()) {
          val connection = res.result()
          connection.queryWithParams(query, params, new Handler[AsyncResult[ResultSet]] {
            override def handle(queryResult: AsyncResult[ResultSet]): Unit = {
              if (queryResult.succeeded()) {
                executeOnResult(queryResult.result())
              } else {
                resultHandler.handle(Future.failedFuture(queryResult.cause()))
              }
            }
          })
        } else {
          resultHandler.handle(Future.failedFuture(FAIL_TO_GET_CONNECTION))
        }
      }
    })
  }


  /**
   * change the default authentication query
   * @param authenticationQuery
   * @return auth provider
   */
  override def setAuthenticationQuery(authenticationQuery: String): JDBCAuth = ???

  /**
   * set the roles query to use
   * @param rolesQuery
   */
  override def setRolesQuery(rolesQuery: String): JDBCAuth = ???

  override def setHashStrategy(strategy: JDBCHashStrategy): JDBCAuth = ???

  override def setRolePrefix(rolePrefix: String): JDBCAuth = ???

  /**
   *
   */
  override def setPermissionsQuery(permissionsQuery: String): JDBCAuth = ???
}

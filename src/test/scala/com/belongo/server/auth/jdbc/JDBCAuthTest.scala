package com.belongo.server.auth.jdbc

import java.sql.DriverManager

import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonObject
import io.vertx.core.{AsyncResult, Handler}
import io.vertx.ext.auth.User
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import io.vertx.rxjava.core.Vertx
import io.vertx.rxjava.ext.jdbc.JDBCClient
import org.junit.runner.RunWith
import org.junit.{Before, Test}

/**
 * Created by Simi on 23.10.2015.
 */

@RunWith(classOf[VertxUnitRunner])
class JDBCAuthTest  {


  var vertx:Vertx = _
  var server:HttpServer = _

  @Before
  def createDB(): Unit = {
    vertx = Vertx.vertx()
   // server = vertx.createHttpServer()

    val conn = DriverManager.getConnection("jdbc:hsqldb:mem:test?shutdown=true", "SA", "")
    DB.SQL.foreach(SQL => {
       conn.createStatement().execute(SQL)
    })
    conn.close()
  }


  def config():JsonObject = {
    new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");
  }



  @Test
  def testAuthenticateSuceed(context: TestContext): Unit = {
      val async = context.async()
      val client = JDBCClient.createShared(vertx, config())
      val authProvider = JDBCAuthFactory.create(client)

      // Auth
      val authInfo = new JsonObject()
      authInfo.put("username", "simi")
              .put("password", "pro")

      authProvider.authenticate(authInfo, new Handler[AsyncResult[User]] {
        override def handle(event: AsyncResult[User]): Unit = {
          context.assertNotNull(event.result())
          async.complete()
        }
      })
  }

  @Test
  def testAuthenticationFailed(context:TestContext): Unit = {
    val async = context.async()
    val client = JDBCClient.createShared(vertx, config())
    val authProvider = JDBCAuthFactory.create(client)

    // Auth
    val authInfo = new JsonObject()
    authInfo.put("username", "simi")
      .put("password", "wrong password")

    authProvider.authenticate(authInfo, new Handler[AsyncResult[User]] {
      override def handle(event: AsyncResult[User]): Unit = {
        context.assertEquals(Messages.INVALID_USER_NAME_OR_PASSWORD,event.cause().getMessage)
        async.complete()
      }
    })
  }



}

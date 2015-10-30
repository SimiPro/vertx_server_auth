package com.belongo.server.main

import com.belongo.server.auth.BelongoUser
import com.belongo.server.auth.jdbc.JDBCAuthFactory
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.{Json, JsonObject}
import io.vertx.core.{AsyncResult, Handler, AbstractVerticle}
import io.vertx.ext.auth.User
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.{RoutingContext, Router}

import scala.collection.mutable


/**
 * Created by Simi on 24.10.2015.
 */
class AuthentificationVerticle extends AbstractVerticle{


  def jdbcClientConfig():JsonObject = {
    new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");
  }


  def createSomeUsers():mutable.LinkedHashMap[Integer, BelongoUser] = {
    val userMap = mutable.LinkedHashMap[Integer, BelongoUser]()
    userMap.put(1, new BelongoUser("simi", "pro"))
    userMap.put(2, new BelongoUser("simi2", "pro2"))
    userMap
  }

  override def start(): Unit = {
     val jdbcClient = JDBCClient.createShared(vertx,jdbcClientConfig())
     val authProvider = JDBCAuthFactory.create(jdbcClient)
     val router = Router.router(vertx)

    val users = createSomeUsers()

    router.get("/api/users").handler(new Handler[RoutingContext] {
      override def handle(rtx: RoutingContext): Unit = {
        rtx.response().putHeader("content-type", "application/json;").end(Json.encodePrettily(users.values))
      }
    })

    router.post("/api/newUser").handler(new Handler[RoutingContext] {
      override def handle(request: RoutingContext): Unit = {

      }
    })

    router.post("/api/login").handler(new Handler[RoutingContext] {
      override def handle(rtx: RoutingContext): Unit = {
        val credentials = rtx.getBodyAsJson
        authProvider.authenticate(credentials, new Handler[AsyncResult[User]] {
          override def handle(e: AsyncResult[User]): Unit = {
            if (e.succeeded()) {
              rtx.response().end("Yey sucessfully authenticated")
            }else{
              rtx.response().end(e.cause().getMessage)
            }
          }
        })
      }
    })



    vertx.createHttpServer().requestHandler(new Handler[HttpServerRequest] {
      override def handle(e: HttpServerRequest): Unit = {
        println(e)
        router.accept(e)
      }
    }).listen(8888)
  }
}

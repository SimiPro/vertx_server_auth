package com.belongo.server.main

import com.belongo.server.auth.jdbc.JDBCAuthFactory
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.JsonObject
import io.vertx.core.{Handler, AbstractVerticle}
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.{RoutingContext, Router}


/**
 * Created by Simi on 24.10.2015.
 */
class AuthentificationVerticle extends AbstractVerticle{


  def jdbcClientConfig():JsonObject = {
    new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");
  }


  override def start(): Unit = {
     val jdbcClient = JDBCClient.createShared(vertx,jdbcClientConfig())
     val authProvider = JDBCAuthFactory.create(jdbcClient)
     val router = Router.router(vertx)

    router.post("/api/newUser").handler(new Handler[RoutingContext] {
      override def handle(request: RoutingContext): Unit = {

      }
    })



    vertx.createHttpServer().requestHandler(new Handler[HttpServerRequest](){
      override def handle(request: HttpServerRequest): Unit = {
        request.response().end("Successfully authenticated")
      }
    }).listen(8888)
  }
}

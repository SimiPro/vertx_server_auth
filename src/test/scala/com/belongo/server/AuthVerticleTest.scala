package com.belongo.server

import java.net.ServerSocket

import com.belongo.server.main.AuthentificationVerticle
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.json.{Json, JsonObject}
import io.vertx.core.{Handler, DeploymentOptions, Vertx}
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.{Test, After, Before}
import org.junit.runner.RunWith

/**
 * Created by simipro on 30/10/15.
 */

@RunWith(classOf[VertxUnitRunner])
class AuthVerticleTest {
  var vertx: Vertx =  Vertx.vertx()
  val socket = new ServerSocket(0)
  val port = socket.getLocalPort
  socket.close()


  @Test
  def testGetUsers(context: TestContext): Unit = {
    val async = context.async()
      vertx.createHttpClient().getNow(port,"localhost","/api/users", new Handler[HttpClientResponse](){
        override def handle(response: HttpClientResponse): Unit = {
          context.assertTrue(response.headers().get("content-type").contains("application/json"))
          response.bodyHandler(new Handler[Buffer] {
            override def handle(rspBuffer: Buffer): Unit = {
              println(rspBuffer.toString())
              async.complete()
            }
          })
        }
      })
  }




  @Before
  def setUp(context: TestContext): Unit = {
    val options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port))
    vertx.deployVerticle(classOf[AuthentificationVerticle].getName,options, context.asyncAssertSuccess[String]())
  }


  @After
  def tearDown(context: TestContext): Unit = {
    vertx.close(context.asyncAssertSuccess())
  }


}

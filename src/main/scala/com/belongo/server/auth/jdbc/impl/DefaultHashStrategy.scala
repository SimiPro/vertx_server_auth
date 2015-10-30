package com.belongo.server.auth.jdbc.impl

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import com.belongo.server.auth.jdbc.JDBCHashStrategy
import io.vertx.core.json.JsonArray

/**
 * Created by Simi on 23.10.2015.
 */
class DefaultHashStrategy extends JDBCHashStrategy {


  override def computeHash(password: String, salt: String): String = {
    DefaultHashStrategy.computeHash(password, salt)
  }

  override def getSalt(row: JsonArray): String = row.getString(1)

  override def getHashesStoredPwd(row: JsonArray): String = row.getString(0)
}

object DefaultHashStrategy {
  val HEX_CHARS = "0123456789ABCDEF".toCharArray()

  def computeHash(password: String, salt: String): String = {
    val md = MessageDigest.getInstance("SHA-512")
    val hashTo = md.digest((password + salt).getBytes(StandardCharsets.UTF_8))
    bytesToHex(hashTo)
  }


  def bytesToHex(bytes:Array[Byte]): String = {
    val chars = new Array[Char](bytes.length * 2)
    for (i <- 0 until bytes.length) {
      val x = 0xFF & bytes(i)
      chars(i*2) = HEX_CHARS(x >>> 4)
      chars(1 + i * 2) = HEX_CHARS(0x0F & x)
    }
    new String(chars)
  }
}
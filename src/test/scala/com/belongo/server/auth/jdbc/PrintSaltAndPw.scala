package com.belongo.server.auth.jdbc

import java.security.SecureRandom

import com.belongo.server.auth.jdbc.impl.DefaultHashStrategy

/**
 * Created by Simi on 24.10.2015.
 */
object PrintSaltAndPw {

  def main(args: Array[String]) {
      val salt = genSalt()
      val hashedPwd = DefaultHashStrategy.computeHash("pro", salt)
      println("Pwd: " + hashedPwd)
      println("Salt: " + salt)

  }

  def genSalt():String = {
    val r = new SecureRandom()
    val salt = new Array[Byte](32)
    r.nextBytes(salt)
    DefaultHashStrategy.bytesToHex(salt)
  }

}

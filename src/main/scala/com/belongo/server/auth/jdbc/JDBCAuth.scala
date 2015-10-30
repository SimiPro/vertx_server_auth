package com.belongo.server.auth.jdbc

import com.belongo.server.auth.jdbc.impl.JDBCAuthImpl
import io.vertx.ext.auth.AuthProvider
import io.vertx.rxjava.ext.jdbc.JDBCClient


trait JDBCAuth extends AuthProvider {
  /**
   * Default query used for authentication
   */
  var DEFAULT_AUTHENTICATE_QUERY = "SELECT PASSWORD, PASSWORD_SALT FROM USER WHERE USERNAME = ?"

  /**
   * default query to retrieve all roles from user
   */
  var DEFAULT_ROLES_QUERY = "SELECT ROLE FROM USER_ROLES WHERE USERNAME = ?"

  /**
   * default query to retrieve all permissions for the role
   */
  var DEFAULT_PERMISSIONS_QUERY = "SELECT PERM FROM ROLES_PERMS RP, USER_ROLES UR WHERE UR.USERNAME = ? AND UR.ROLE = RP.ROLE"

  /**
   * the default role prefix
   */
  var DEFAULT_ROLE_PREFIX = "role:"


  /**
   * change the default authentication query
   * @param authenticationQuery
   * @return auth provider
   */
  def setAuthenticationQuery(authenticationQuery: String): JDBCAuth


  /**
   * set the roles query to use
   * @param rolesQuery
   */
  def setRolesQuery(rolesQuery: String): JDBCAuth


  /**
   *
   */

  def setPermissionsQuery(permissionsQuery: String): JDBCAuth


  def setRolePrefix(rolePrefix: String): JDBCAuth


  def setHashStrategy(strategy: JDBCHashStrategy ): JDBCAuth


}

object JDBCAuthFactory {

  def create(client: JDBCClient): JDBCAuth = {
    new JDBCAuthImpl(client)
  }

  def create(client: io.vertx.ext.jdbc.JDBCClient): JDBCAuth = {
    new JDBCAuthImpl(new JDBCClient(client))
  }

}
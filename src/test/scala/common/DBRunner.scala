package common

/**
  * Created by ykalinicheva on 7/6/16.
  */

import scala.slick.driver.MySQLDriver
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery

trait DBRunner {

  private val appConfig = AppConfig
  private val dbDriver = "com.mysql.jdbc.Driver"

  protected lazy val db = Database.forURL(
    url = appConfig.DB.url,
    user = appConfig.DB.userName,
    password = appConfig.DB.userPassword,
    driver = dbDriver)

  Class.forName(dbDriver)

  protected def dbGetLastId(implicit session: MySQLDriver.backend.Session): Int =
    //TODO rework to safe method
    StaticQuery.queryNA[Int]("""SELECT LAST_INSERT_ID();""").list.head
}
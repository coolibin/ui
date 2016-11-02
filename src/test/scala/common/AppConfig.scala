package common

import com.typesafe.config.ConfigFactory

object AppConfig {
  lazy private val log = LogManager.getLogger(AppConfig.getClass.getName)
  lazy val config = ConfigFactory.load()

  lazy val autoRun = getString("autorun")


  def getString(parameterName: String, defaultValue: Option[String] = None): String = {
    if (config.hasPath(parameterName)) {
      config.getString(parameterName)
    } else {
      defaultValue.getOrElse {
        parameterReadingError(parameterName)
      }
    }
  }

  def getStringOpt(parameterName: String): Option[String] = {
    if (config.hasPath(parameterName)) {
      Some(config.getString(parameterName))
    } else {
      None
    }
  }

  def getBoolean(parameterName: String, defaultValue: Option[Boolean] = None): Boolean = {
    if (config.hasPath(parameterName)) {
      config.getBoolean(parameterName)
    } else {
      defaultValue.getOrElse {
        parameterReadingError(parameterName)
      }
    }
  }

  def getBooleanOpt(parameterName: String): Option[Boolean] = {
    if (config.hasPath(parameterName)) {
      Some(config.getBoolean(parameterName))
    } else {
      None
    }
  }

  def getInt(parameterName: String, defaultValue: Option[Int] = None): Int = {
    if (config.hasPath(parameterName)) {
      config.getInt(parameterName)
    } else {
      defaultValue.getOrElse {
        parameterReadingError(parameterName)
      }
    }
  }

  def getIntOpt(parameterName: String): Option[Int] = {
    if (config.hasPath(parameterName)) {
      Some(config.getInt(parameterName))
    } else {
      None
    }
  }

  def parameterReadingError(parameterName: String) = {
    log.error(s"Attempted to read nonexistent parameter: $parameterName")
    throw new Exception(s"Parameter $parameterName is not defined")
  }


  object UI {
    lazy val baseURL = Global.dropTail(getString("nateraqa.ui.baseurl"), "/")
    lazy val baseUrlExtranetAndLims = getString("nateraqa.ui.baseUrlExtranetAndLims")
  }

  object Login {
    lazy val username: String = getString("nateraqa.login.username")
    lazy val password: String = getString("nateraqa.login.password")
    lazy val url = getString("nateraqa.login.url")
  }

  object DB {
    lazy val url: String = getString("nateraqa.db.url")
    lazy val userName: String = getString("nateraqa.db.username")
    lazy val userPassword: String = getString("nateraqa.db.password")
  }

  object WebDriver {

    lazy val runLocally: Boolean = getBoolean("nateraqa.webdriver.runlocally", Some(false))
    lazy val driverName: String = getString("nateraqa.webdriver.drivername", Some("chrome"))
    lazy val selHubURL = Global.dropTail(getString("nateraqa.webdriver.selhuburl"), "/")
    lazy val includeSeleniumLog: Boolean =
      getBoolean("nateraqa.webdriver.includeSeleniumLog", Some(false))

    lazy val screenshotFolder: String = getString("nateraqa.webdriver.screenshotfolder")
  }

  object Feed {
    lazy val feedFolder = getString("feed.feedFolder")
    lazy val fieldDelimiter = getString("feed.fieldDelimiter")
    lazy val listSplitter = getString("feed.listSplitter")
    lazy val pairSplitter = getString("feed.pairSplitter")
  }
}

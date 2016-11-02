package common

import org.slf4j.LoggerFactory

object LogManager {

  def getLogger(loggerName: String) = LoggerFactory.getLogger(loggerName)
}
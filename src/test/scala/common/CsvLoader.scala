package common

import com.gingersoftware.csv.ObjectCSV

import scala.reflect.runtime.universe.TypeTag

/**
  * Created by akalinichev on 7/1/16.
  */
class CsvLoader extends LogRunner {

  private val appConfig = AppConfig
  private var fieldDelimiter = appConfig.Feed.fieldDelimiter.charAt(0)

  def withFieldDelimiter(delimiter: String) = {
    fieldDelimiter = delimiter.charAt(0)
  }

  /**
    * Loads data from a csv file by loader name
    *
    * @param loaderName - can be either full class name of just a random name
    *                   System will also check if a config file contains a path parameter
    *                   with the name <loaderName>_input. In that case the one from
    *                   a confing file will be used
    * @tparam T - type for the csv file record
    * @return list of uploaded records
    */
  def loadForName[T: TypeTag](loaderName: String): List[T] = {

    log.debug("Loading csv file for loader: " + loaderName)

    var inputPath = appConfig.Feed.feedFolder
      .concat("/")
      .concat(if (loaderName.contains('.')) loaderName.split('.').last else loaderName)
      .concat(".csv")

    // file path might be overwritten in config
    try {
      if (appConfig.getString(inputPath + "_input").nonEmpty) {
        inputPath = appConfig.getString(inputPath + "_input")
      }
    } catch {
      case _: Throwable =>
        log.debug("Configuration parameter is not found, the default file name will be used: "
          .concat(inputPath))
    }

    loadFromFile[T](inputPath)
  }

  /**
    * Loads data from a csv file into a typed list
    *
    * @param inputPath
    * @tparam T - type for a csv record
    * @return list of uploaded records
    */
  def loadFromFile[T: TypeTag](inputPath: String): List[T] = {

    log.debug(s"Loading data from $inputPath ...")

    val csvConfig = com.gingersoftware.csv.Config(
      delimiter = this.fieldDelimiter)

    val dataList = ObjectCSV(csvConfig)
      .readCSV[T](
      inputPath = inputPath
    ).toList
    dataList
  }
}

object CsvLoader {
  def apply(): CsvLoader = new CsvLoader()
}

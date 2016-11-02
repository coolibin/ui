package common

trait CSVRunner {

  def loadFromCSV[T: Manifest](fileName: String): List[T] = {
    CsvLoader().loadFromFile[T](FileManager.fileFromFeedFolder(fileName))
  }
}

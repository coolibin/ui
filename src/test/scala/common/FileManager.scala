package common

import java.io._
import java.nio.file.{Paths, StandardCopyOption}

object FileManager extends ResourceManager {

  lazy private val log = LogManager.getLogger(FileManager.getClass.getName)

  def pathJoin(path: String, fileName: String): String = {
    Paths.get(path, fileName).toAbsolutePath.toString
  }

  def uploadFileToServer(fileFrom: String, serverTo: String, fileTo: String): Boolean = {

    var ret: Boolean = false

    using(new SSHManager(
      AppConfig.Login.username.split("""@""")(0),
      AppConfig.Login.password,
      getIpByName(serverTo),
      "")) {
      instance => {

        log.debug("Connecting to {}...", serverTo)

        val errorMessage: String = instance.connect
        if (errorMessage == null) {
          log.debug("File copied from {}", fileFrom)
          ret = instance.sshUploadFileToServer(fileFrom, fileTo) == 0
          if (ret) log.debug("File copied to {}", fileTo)
        } else {
          log.error(errorMessage)
        }
        ret
      }
    }
  }

  /**
    * Only works on OSX or xUnix computers
    *
    * @param serverName
    * @return
    */
  def getIpByName(serverName: String): String = {
    log.debug("Getting an IP address for {}", serverName)
    var ip: String = ""
    try {
      val cmd: Array[String] = Array("""/bin/sh""", "-c", "ping -c1 " + serverName + """ | grep -Eo -m1 '([0-9]*\.){3}[0-9]*'""")
      val p: Process = Runtime.getRuntime.exec(cmd)
      p.waitFor

      ip = new BufferedReader(new InputStreamReader(p.getInputStream)).readLine()
    }
    catch {
      case e: Exception => {
        log.error(e.getMessage)
      }
    }
    ip
  }

  /**
    * Loads external file into an array of Byte and converts it into array of Int
    * as it is required for the accessioning picture processing
    *
    * @param inputFile
    * @return array of Int
    */
  def loadImg(inputFile: String): List[Int] = {
    val fi = new java.io.File(inputFile)
    val imageBytes = java.nio.file.Files.readAllBytes(fi.toPath)
    imageBytes.toList.map(d => d.toInt)
  }

  /**
    * Copies a text file with pattern replacement
    *
    * @param sourceFileName
    * @param destinationFileName
    * @param replacement
    */
  def copyTextFileWithTextReplacement(
    sourceFileName: String,
    destinationFileName: String,
    replacement: Map[String, String]) = {

    import java.nio.charset.StandardCharsets
    import java.util.Scanner

    using(new Scanner(Paths.get(sourceFileName), StandardCharsets.UTF_8.name())) { scanner =>
      using(new FileWriter(destinationFileName)) { writer =>
        while (scanner.hasNextLine) {
          var line = scanner.nextLine()
          replacement.keys.foreach { fromText =>
            line = ("(\\" + fromText + ")").r.replaceAllIn(line, replacement.get(fromText).get)
          }
          writer.write(line + "\r")
        }
      }
    }
  }

  def saveTextToFile(text: String, fileName: String) = {
    using(new FileWriter(fileName)) { writer =>
      writer.write(text)
    }
  }

  def copyFile(fromFile: String, toFile: String) = {
    java.nio.file.Files.copy(
      Paths.get(fromFile),
      Paths.get(toFile),
      StandardCopyOption.REPLACE_EXISTING)
  }

  def deleteFile(fileToDelete: String) = {
    java.nio.file.Files.deleteIfExists(Paths.get(fileToDelete))
  }

  def fileFromFeedFolder(fileName: String): String = {
    FileManager.pathJoin(AppConfig.Feed.feedFolder, fileName)
  }
}

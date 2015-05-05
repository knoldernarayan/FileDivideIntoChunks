package fileStream

import java.io.RandomAccessFile
import org.scalatest.FunSuite

class FileReadTestSpec extends FunSuite {
  val defaultBlockSize: Long = 76 * 1024
  val randomAccessFile = new RandomAccessFile("/home/knoldus/Downloads/decoodaurlfiles/starbucks_2k.csv", "r")
  val fileSize = randomAccessFile.length()
  test("test for method correctIndex") {
    assert(77801 === FileReadTest.correctIndex(randomAccessFile,defaultBlockSize, -1))
  }
}
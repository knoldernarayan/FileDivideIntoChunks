package fileStream

import java.io.RandomAccessFile
import org.slf4j.LoggerFactory

object FileReadTest extends App {

  val logger = LoggerFactory.getLogger(this.getClass)

  val defaultBlockSize: Long = 76 * 1024
  val randomAccessFile = new RandomAccessFile("/home/knoldus/Downloads/decoodaurlfiles/starbucks_2k.csv", "r")
  val fileSize = randomAccessFile.length()
  val noOfChunk = Math.ceil((fileSize / defaultBlockSize.toDouble)).toInt

  (0 to noOfChunk - 1) foreach { chunkIndex =>
    val seek = chunkIndex * defaultBlockSize
    val correctedSeek = if (seek == 0) seek else correctIndex(randomAccessFile, seek, -1)
    val correctedTailIndex = if ((seek + defaultBlockSize) == fileSize) (correctIndex(randomAccessFile, seek + defaultBlockSize, +1))
    else correctIndex(randomAccessFile, seek + defaultBlockSize, -1)
    val correctedBufferSize = (correctedTailIndex - correctedSeek).toInt
    val rawString = readChunk(correctedSeek, correctedBufferSize)
    System.out.println("number of lines in chunk" + (chunkIndex + 1) + " is " + rawString.split(System.getProperty("line.separator")).length + " starting index " + correctedSeek + " end index " + correctedTailIndex + " correctedBufferSize " + correctedBufferSize)
  }

  private def readChunk(correctedSeek: Long, correctedBufferSize: Int): String = {
    val byteBuffer = new Array[Byte](correctedBufferSize)
    randomAccessFile.seek(correctedSeek)
    randomAccessFile.read(byteBuffer)
    new String(byteBuffer)

  }

  def correctIndex(randomAccessFile: RandomAccessFile, start: Long, move: Int): Long = {
    randomAccessFile.seek(start)
    val read = randomAccessFile.read()
    if (read == '\n' || read == -1) start
    else correctIndex(randomAccessFile, start + move, move)
  }

}
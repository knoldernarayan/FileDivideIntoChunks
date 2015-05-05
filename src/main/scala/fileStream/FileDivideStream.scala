package fileStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import scala.annotation.tailrec
import org.slf4j.LoggerFactory

class FileDivide {
  
val logger = LoggerFactory.getLogger(this.getClass)

  def readFileViaChannel(buffer: ByteBuffer, file: RandomAccessFile): Int = {

    val channel = file.getChannel()
    var fileId = 1
    var filePosition = 0l

    def readFile(channel: FileChannel): Int = {
      if (channel.read(buffer) > 0) {
        if (buffer.get(buffer.limit() - 1) == 10) {
          val (newFileId, newFilePosition) = wirteOnChannel(fileId, filePosition, buffer, file)
          filePosition = newFilePosition
          fileId = newFileId

        } else {

          val (newFileId, newFilePosition) = wirteOnChannelViaNewLine(fileId, filePosition, buffer, file)
          filePosition = newFilePosition
          buffer.clear()
          fileId = newFileId
        }
        readFile(channel.position(filePosition))
      } else {
        //channel.close()
        fileId = fileId-1
        -1
      }
    }
    readFile(channel)
    fileId
  }

  def wirteOnChannelViaNewLine(fileId: Int, filePosition: Long, buffer: ByteBuffer, file: RandomAccessFile) = {
    buffer.flip()
    val chunkFile = new File("newFile" + fileId + ".csv")
    val chunkFileChannel = new FileOutputStream(chunkFile)
      .getChannel()
    val channel = file.getChannel().position(filePosition)
    channel.read(buffer)
    //System.out.println("position  " + filePosition + " file no  " + fileId + " buffer Size " + buffer.limit() + "  last charactor " + buffer.get(buffer.limit() - 1).toChar)
    var currentChunkSize = 0l
    var index = (buffer.limit() - 1)
    System.out.println("position  " + filePosition + " file no  " + fileId + " currentBuffer Size " + buffer.limit() + "  last charactor " + buffer.get(buffer.limit() - 1).toChar)
    @tailrec
    def findNewLinePostion: Int = {
      if (index > 0) {
        if (buffer.get(index) == 10) {System.out.println("position  " + filePosition + " file no  " + fileId + " buffer Size " + buffer.limit() + "  last charactor " + buffer.get(buffer.limit() - 1).toChar)
          currentChunkSize = (channel.position() - ((buffer
            .limit() - 1) - index)) - filePosition
          channel.transferTo(filePosition, currentChunkSize,
            chunkFileChannel)
          System.out.println("channel position " + channel.position() + " index  " + index + " currentChunkSize " + currentChunkSize)
          index = 0
          //logger.info("chunk size"+currentChunkSize)
        }
        index = index - 1
        findNewLinePostion
      } else 0
    }
    findNewLinePostion
   // System.out.println("position  " + (filePosition + currentChunkSize) + "file no  " + fileId + " buffer Size " + buffer.limit() + "  last charactor " + buffer.get(buffer.limit() - 1).toChar)
    buffer.clear()
    chunkFileChannel.close()
    (fileId + 1, filePosition + currentChunkSize)
  }

  def wirteOnChannel(fileId: Int, filePosition: Long, buffer: ByteBuffer, file: RandomAccessFile) = {
    buffer.flip()
    val chunkFile = new File("newFile" + fileId + ".csv")
    val chunkFileChannel = new FileOutputStream(chunkFile)
      .getChannel()
    val channel = file.getChannel()
    channel.position(filePosition)
    channel.read(buffer)
    chunkFileChannel.write(buffer)
    buffer.clear()
    chunkFileChannel.close()
    (fileId + 1, filePosition + buffer.limit())
  }

}

object FileDivideStream extends App {

  val file = new RandomAccessFile(
    "/home/knoldus/Downloads/decoodaurlfiles/starbucks_2k.csv", "r")
  val channel = file.getChannel()
  System.out.println("postion "+channel.position())
  val bufferLength = (76 * 1024)
  val buffer = ByteBuffer.allocate(bufferLength)
  val fileDivide = new FileDivide
  System.out.println(fileDivide.readFileViaChannel(buffer, file)+" postion")
  file.close()
  channel.close()

}
package fileStream

import org.scalatest.FunSuite
import java.io.RandomAccessFile
import java.nio.ByteBuffer

/*class FileDivideStreamSpec extends FunSuite {

  val file = new RandomAccessFile(
    "/home/knoldus/Downloads/decoodaurlfiles/starbucks_2k.csv", "r")
  val channel = file.getChannel()
  val bufferLength = 76 * 1024
  val buffer = ByteBuffer.allocate(bufferLength)
  val fileDivide = new FileDivide
  var numberOfChunks = if (file.length() % bufferLength > 0) (file.length() / bufferLength) + 1 else (file.length() / bufferLength)

  //System.out.println(" file parts "+(file.length()/bufferLength)+"remainder "+(file.length()%bufferLength))

    test("test for method readFileViaChannel") {
    assert(numberOfChunks === fileDivide.readFileViaChannel(buffer, file))
  }
 
  test("test for method wirteOnChannel") {
    assert((1,bufferLength) === fileDivide.wirteOnChannel(0, 0l, buffer, file))
  }

  test("test for method wirteOnChannelViaNewLine") {
    assert((1, 77802) === fileDivide.wirteOnChannelViaNewLine(0, 0l, buffer, file))
  }

}
*/
object Test extends App {
  
  val file = new RandomAccessFile(
    "/home/knoldus/Downloads/decoodaurlfiles/starbucks_2k.csv", "r")
  val channel = file.getChannel()
  val bufferLength = 76 * 1024
  val buffer = ByteBuffer.allocate(bufferLength)
  val fileDivide = new FileDivide
  var numberOfChunks = if (file.length() % bufferLength > 0) (file.length() / bufferLength) + 1 else (file.length() / bufferLength)
  val s = fileDivide.wirteOnChannelViaNewLine(0, 0l, buffer, file)
  println(s)
  
}
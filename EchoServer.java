import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class EchoServer {
  public static void main(String[] args) {
    try { 
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // open the ServerSocketChannel
      serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 1234)); // bind to the port
      serverSocketChannel.socket().setReuseAddress(true); // enable reuse addresses
      serverSocketChannel.configureBlocking(false); // set non-blocking mode

      Selector selector = Selector.open(); // open the selector
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // register the server as accepter

      ByteBuffer buffer = ByteBuffer.allocateDirect(1024); // allocate the buffer   

      while (true) {
        int readyChannels = selector.select(10000); // search for ready channels
        if (readyChannels == 0) break; // if no channels are ready in 10 seconds then close the server

        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator(); // iterator of selected keys

        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next(); // get a key

          keyIterator.remove(); // remove the iterator

          if (key.isAcceptable()) { 
            serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocketChannel.accept(); // accept the client
            client.configureBlocking(false); // select non-blocking mode
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE); // register the client for reading and writing operations
          }  

          if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();

            buffer.clear(); // clear the buffer
            client.read(buffer); // read from the socket and write into the buffer
            buffer.flip(); // prepare for reading

            int msgLength = buffer.getInt(); // get the message length
            byte[] msgBytes = new byte[msgLength]; // get the message byte array
            buffer.get(msgBytes); // get msgBytes bytes from the buffer
            String msg = new String(msgBytes); // get the message from the client

            if (msg.equals("CLOSED")) { // if msg == "CLOSED"
              client.close(); // close the client
              break; // break this iteration
            }

            String echo = msg + " echoed by Server"; // modify the message

            buffer.clear(); // clear the buffer
            buffer.putInt(echo.length()); // write the echo length into the buffer
            buffer.put(echo.getBytes()); // put the message on the buffer
          }

          if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();

            buffer.flip(); // prepare for read
            while (buffer.hasRemaining()) client.write(buffer); // write to client
            buffer.flip(); // prepare for writing
          }
        }
      }
      selector.close(); // close the selector
      serverSocketChannel.close(); // close the ServerSocketChannel
    }
    catch (IOException e) {e.printStackTrace();}
  }
}
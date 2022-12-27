import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
  public static void main(String[] args) {
    try (SocketChannel client = SocketChannel.open()) { // open the SocketChannel
      client.configureBlocking(true); // select blocking mode

      client.connect(new InetSocketAddress("localhost", 1234));  // connect to server

      try (BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in))) { // try to create a new BufferedReader to read user input

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024); // allocate the buffer

        while (true) {
          System.out.println("Insert a message for the server: ");
          String msg = userIn.readLine(); // read message from input
          int msgLength = msg.length(); // message length

          if (msg.equals("EXIT")) {
            buffer.clear(); // clear the buffer
            buffer.putInt(6); // write the message length into the buffer
            buffer.put("CLOSED".getBytes()); // write the message into the buffer

            buffer.flip(); // prepare for reading
            client.write(buffer); // read from the buffer and write it to the socket
            buffer.flip(); // prepare for writing 

            break; // exit condition
          }

          if (!msg.equals("")) {
            buffer.clear(); // clear the buffer
            buffer.putInt(msgLength); // write the message length into the buffer
            buffer.put(msg.getBytes()); // write the message into the buffer

            buffer.flip(); // prepare for reading
            client.write(buffer); // read from the buffer and write it to the socket
            buffer.flip(); // prepare for writing

            buffer.clear(); // clear the buffer
            client.read(buffer); // read from the socket and write into the buffer 
            buffer.flip(); // prepare for reading

            int echoLength = buffer.getInt(); // get the echo length
            byte[] echoBytes = new byte[echoLength]; // get the echo byte array
            buffer.get(echoBytes); // get echoBytes bytes from the buffer
            String echo = new String(echoBytes); // get the echo from the server
            System.out.println(echo + "\n");
          }

          else System.out.println("Please, insert a non-empty message for the server...\n");
        }
      }
    }
    catch (IOException e) {e.printStackTrace();}
  }
}
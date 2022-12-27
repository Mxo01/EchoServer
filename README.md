
# Echo Server

Simple Echo Server which receives a series of messages from 
the Client and sends them back adding "echoed by Server" to 
the message. Both Client and Server take advantage of the [java.nio](https://docs.oracle.com/javase/7/docs/api/java/nio/package-frame.html) 
package provided by Java using ByteBuffers to store sent messages to 
be sent via [SocketChannel](https://docs.oracle.com/javase/7/docs/api/java/nio/channels/SocketChannel.html).

## Installation (Windows)

First of all you need to install jdk from [Downloads](https://www.oracle.com/java/technologies/downloads/).

So check everything is ok:
```bash
  javac --version
  java --version
```
If you're using Visual Studio Code check this [Extension](https://code.visualstudio.com/docs/java/extensions).

On Linux it is much simpler and you just have to write a few lines of code on the terminal and you are done.
    
## Authors

For doubts or concerns mariodimodica.01@gmail.com. 
- [@mxo01](https://github.com/Mxo01)

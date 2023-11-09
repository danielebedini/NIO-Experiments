# NIO Experiments
 This is a simple example of a client-server communication using the NIO java library.

### How does it work?

##### Server

This is how the server works: 
- opens the selector: _Selector.open()_
- always listening at the address "localhost" at the port 9999 
- his socket is non-blocking: _serverSocketChannel.configureBlocking(false)_
- initialize _ops_, the valid operations that the selector can select, saved in the _selectionKeys_

Then the enters a _while(true)_ and keeps the connection with the client.

In this _while_:
- sets an _iterator_ in order to iterate in the selector, the selector is initialized as an _HashSet_, a Set from the Java Collections, so we have to use an iterator for scanning the set
- selects the _key_ relative to the client
- if the _key_ is acceptable then it's the first time the server sees the client, so it accepts the connection
    - the server configures the operations as non-blocking and registers the _key_ in the _selector_ 
- if the _key_ is readable then the client wants to communicate with the server, so the server reads the message
    - the server needs to take the channel from the socket relative to the client with _(SocketChannel) mykey.channel_
    - it allocates a bytebuffer for reading the client message if there are bytes to read
    - first flips the buffer poiters in order to start reading (sets _position_ and _limit_ poiter)
    - when the server reads "end" or there are no bytes left to read, it closes the connection with the client

##### Client

- opens the _socketChannel_ at the address "localhost" at the port 9999
- sets up an _ArrayList<String>_ 
- allocates the _bytebuffer_ for writing messages in the channel, in order to communicate with the server
- in a _for each_ iterates in the data array and writes the strings in the buffer relative to the channel
- after writing the message in the buffer it calls _buffer.clear()_ for setting up the poiters for a new write in the next iteration (resets the _position_ pointer)
- closes connection after sending all the data

---

This is a simple example for understanding how non-blocking communications between clients and servers work with the library Java NIO.
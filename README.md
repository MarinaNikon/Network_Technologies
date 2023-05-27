# **Network_Technologies**

#### Author: Marina Nikonchuk
#### Final Project for the Module "Network Technologies" of the H.Dip in Computer Science in Software Development - GMIT

### Project Description
This is simple network-based multi threaded chat application written in Java, using the Java Socket API. With the help of this application clients communicate with the server and through the server can communicate with each other.
The server should start first and listens for any incoming connections from a client. Once a connection has been established, both client and server use threads to send and receive messages which allows one to send multiple messages without upsetting flow of communication.

### Running project
The Server should be run first. When the message "Server Started" appears, the client could be run. 
The client can be launched multiple times, thus adding a new client to the conversation.
Each new client receives 10 previous messages upon connection. In order to exit the conversation, the client needs to write "quit".

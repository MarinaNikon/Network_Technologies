package ie.gmit.dip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * @author Marina Nikonchuk
 * Server and client applications with the Java Socket API
 */

public class Server {

    public static final int PORT = 1313; // // random port (can be any number from 1024 to 65535)
    public static LinkedList<ChatServer> serverList = new LinkedList<>(); // a list of all server threads, each listening to its client
    public static Story story; // chatting history
    
       
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        story = new Story();
        System.out.println("Server Started");
        try {
            while (true) {
                // block the current thread until a new connection is established
                Socket socket = server.accept();
                try {
                    serverList.add(new ChatServer(socket)); // add a new connection to the list
                } catch (IOException e) {
                    // if it fails, the socket will be closed, 
                    // otherwise, the thread will close it.
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}
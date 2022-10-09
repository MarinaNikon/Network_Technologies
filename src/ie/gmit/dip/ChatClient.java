package ie.gmit.dip;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marina Nikonchuk
 * Server and client applications with the Java Socket API
 */

public class ChatClient {
    
	private Socket socket; // initialize socket and input, output streams
    private BufferedReader in;  
    private BufferedWriter out;
    private BufferedReader inputClient; // console read stream
    private String addr; // client ip address
    private int port; // connection port
    private String name; // client name
    private Date time;
    private String dtime;
    private SimpleDateFormat dt;
    
    /**
     * constructor to put ip address and port
     */
    
    public ChatClient(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try { // establish a connection
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failure");
        }
        try {
            // takes input from terminal
            inputClient = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // sends output to the socket
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressName(); // ask client name
            new ReadMessage().start(); // read messages from the socket
            new WriteMessage().start(); // write messages to the socket from the console
        } catch (IOException e) {
            // Socket must be closed on any error except a socket constructor error. 
        	// Otherwise, the socket will be closed in thread run method.
            ChatClient.this.downService();
        }
      
    }
    
    /**
     *entering a name and sending an echo with a greeting to the server
     */
    
    private void pressName() {
        System.out.print("Please enter your name: ");
        try {
            name = inputClient.readLine();
            out.write("Hello " + name + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
        
    }
    
    /**
     * closing socket
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }
    
    /**
     *  reading messages from  server
     */
    private class ReadMessage extends Thread {
        
        public void run() {
            
            String str;
            try {
                while (true) {
                    str = in.readLine(); // waiting for messages from the server
                    if (str.equals("quit")) {
                        ChatClient.this.downService();  
                        break; // exit loop if "quit" was printed
                    }
                    System.out.println(str); // writing a message from server to console
                }
            } catch (IOException e) {
                ChatClient.this.downService();
            }
        }
    }
    
    /** 
     * messages coming from console to server
     */
    public class WriteMessage extends Thread {
        
        
        public void run() {
            while (true) {
                String clientWord;
                try {
                    time = new Date(); // present date
                    dt = new SimpleDateFormat("HH:mm:ss"); // time format
                    dtime = dt.format(time);  
                    clientWord = inputClient.readLine(); // console messages
                    if (clientWord.equals("quit")) {
                        out.write("quit" + "\n");
                        ChatClient.this.downService();  
                        break; // exit loop if "quit" was printed
                    } else {
                        out.write("(" + dtime + ") " + name + ": " + clientWord + "\n"); // sending to the server 
                    }
                    out.flush();  
                } catch (IOException e) {
                    ChatClient.this.downService(); // close the connection
                    
                }
                
            }
        }
    }
}


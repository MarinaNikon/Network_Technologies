package ie.gmit.dip;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

/**
 * @author Marina Nikonchuk
 * Server and client applications with the Java Socket API
 */

public class ChatServer extends Thread {
    
	private Socket socket; // initialize socket and input / output streams
    private BufferedReader in; 
    private BufferedWriter out; 
    
    public ChatServer(Socket socket) throws IOException { // a socket is required to communicate with the client
        this.socket = socket;
        // allow to read / write data
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(out); // the history of the last 10 messages is transferred to the new connection
        start(); // call run ()
    }
    
    public void run() {
        String word;
        try {
            // 
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush(); 
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("quit")) {
                        this.downService(); 
                        break; // exit loop if "quit" was printed
                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    for (ChatServer vr : Server.serverList) {
                        vr.send(word); // the received message is sent to all clients
                    }
                }
            } catch (NullPointerException ignored) {}

            
        } catch (IOException e) {
            this.downService();
        }
    }
    
    /** 
     * sending a message to the client on the specified stream
     * @param msg
     */
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
        
    }
    
    /** 
     * shutting down the server
     * interrupting yourself as a thread and removing from the thread list
     */
    private void downService() {
            try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ChatServer vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}

/**
 * class that stores information about the last 10 (or less) messages
 */

class Story {
    
    private LinkedList<String> story = new LinkedList<>();
    
     
    /** 
     * add a message, if there are more than 10 messages, delete the first one and add a new one
     */
    public void addStoryEl(String el) {
        if (story.size() >= 10) {
            story.removeFirst();
            story.add(el);
        } else {
            story.add(el);
        }
    }
    
    /**
     * @param writer
     */
    
    public void printStory(BufferedWriter writer) {
        if(story.size() > 0) {
            try {
                writer.write("History messages" + "\n");
                for (String vr : story) {
                    writer.write(vr + "\n");
                }
                writer.write("/...." + "\n");
                writer.flush();
            } catch (IOException ignored) {}
            
        }
        
    }
}

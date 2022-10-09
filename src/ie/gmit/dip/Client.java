package ie.gmit.dip;

/**
 * @author Marina Nikonchuk
 * Server and client applications with the Java Socket API
 */

public class Client {
    
    public static String ipAddr = "localhost";
    public static int port = 1313; // be sure to specify the port to which the server is bound
    
    /**
     * creating a client connection with the said address and port number
     */
    
    public static void main(String[] args) {
        new ChatClient(ipAddr, port);
    }
}
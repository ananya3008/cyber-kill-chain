import java.io.IOException;//it helps us manage errors that may occur while trying to connect to ports.
import java.net.Socket;// provides a client-side socket for communication with a server. We use it to attempt
// to connect to different ports.

public class PortScanner {
    public static boolean isPortOpen(String ip, int port, int timeout) 
    {//checks whether a specific port on a given IP address is open.
        try {
            Socket socket = new Socket();
            socket.connect(new java.net.InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1"; //  Sets the IP address of the machine to scan.
        int startPort = 1;//sets the starting port number for the scan
        int endPort = 1024;//sets the end port number for the scan
        int timeout = 200;//Sets the timeout duration to 200 ms.

        for (int port = startPort; port <= endPort; port++) {
            if (isPortOpen(ip, port, timeout)) {
                System.out.println("Port " + port + " is open");
            } else {
                System.out.println("Port " + port + " is closed");
            }
        }
    }
}

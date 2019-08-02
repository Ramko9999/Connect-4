import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {

    private ServerSocket server;
    private Socket connection;
    public DataInputStream input;
    public DataOutputStream output;


    public Server(int PORT){
        try {
            server = new ServerSocket(PORT);
        }
        catch(Exception e){
            System.err.println("error occured when it came to hosting server");
        }
    }

    public void listen() throws InterruptedException {
        try {

            System.out.println("Server is looking for connection...");
            System.out.println(server.getLocalPort());
            connection = server.accept();
            System.out.println("Server has accepted connection");
            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        try{
            Server s = new Server(7777);
            s.listen();

        }
        catch(Exception e){
            System.out.println(e);
        }


    }
}
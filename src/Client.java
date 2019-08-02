import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientSocket;
    public DataInputStream input; //reading data
    public DataOutputStream output; //sending data
    public final String IP_ADDRESS = "192.168.1.8"; //IP Address of computer we will connect to

    public Client(String host, int port){
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Connection Established");
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception e){
            System.out.println("Line 27 @ Client.java");
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        String host = keyboard.nextLine();
        Client c = new Client(host, 7777);
    }


}

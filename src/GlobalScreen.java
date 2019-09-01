import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Scanner;

public class GlobalScreen extends JFrame{
    private JButton client_button;
    private JTextField Ip;
    private JTextField port_client;
    private JButton server_button;
    private JTextField port_server;
    private JPanel Main;
    private JPanel Client;
    private JPanel Server;
    private JLabel Header;
    private boolean isClientClicked;
    private boolean isServerClicked;

    public GlobalScreen(){
        super("GlobalScreen");
        this.setVisible(true);
        this.add(Main);
        this.setSize(600,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client_button.setBorderPainted(false);client_button.setBackground(Color.BLACK);client_button.setOpaque(true);
        server_button.setBorderPainted(false);server_button.setBackground(Color.BLACK);server_button.setOpaque(true);
        Ip.setBackground(Color.BLACK);Ip.setOpaque(true); port_client.setBackground(Color.BLACK); port_client.setOpaque(true);
        port_server.setBackground(Color.BLACK); port_server.setOpaque(true);
        port_client.setEditable(false); port_server.setEditable(false); Ip.setEditable(false);
        listen_for_selection();

    }

    public void listen_for_selection(){
        client_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isClientClicked){
                    client_button.setForeground(Color.GRAY);
                    System.out.println("In side isClient Clicked");
                    String IP = Ip.getText();
                    String client_port = port_client.getText();
                    System.out.println(IP);
                    int port_number = convert_to_number(client_port);
                    System.out.println(port_number);
                    if(port_number == -9999){
                        Header.setText("Enter only numbers in the port field for client");
                    }
                    else{

                        try{
                            System.out.println("In side try catch");
                            Client c = new Client(IP, port_number);
                            System.out.println("Client connection established");
                            isClientClicked = false;
                            new NetworkGame(null, true, true, c.input, c.output);
                        }
                        catch(Exception e1){
                            System.out.println(e1);
                        }

                    }
                }
                else{
                    port_client.setEditable(true); Ip.setEditable(true);
                    client_button.setText("GO");
                    isClientClicked = true;
                }
            }
        });
        server_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isServerClicked){
                    client_button.setForeground(Color.GRAY);
                    String serve_port = port_server.getText();
                    int port_number = convert_to_number(serve_port);
                    if(port_number == -9999){
                        Header.setText("Enter only numbers in the port field for the server");
                    }
                    else{
                        try{
                            Scanner Keyboard = new Scanner(System.in);
                            Server s = new Server(port_number);
                            s.listen();
                            new NetworkGame(null, true, false, s.input, s.output);
                        }
                        catch(Exception e2){
                            System.out.println(e2);
                        }
                    }
                    ;
                }
                else{
                    port_server.setEditable(true);
                    server_button.setText("GO");
                    isServerClicked = true;
                }
            }
        });
    }

    public int convert_to_number(String s){
        int total_length = s.length() - 1;
        int value = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put(new Character('0'), new Integer(0));
        map.put(new Character('1'), new Integer(1));
        map.put(new Character('2'), new Integer(2));
        map.put(new Character('3'), new Integer(3));
        map.put(new Character('4'), new Integer(4));
        map.put(new Character('5'), new Integer(5));
        map.put(new Character('6'), new Integer(6));
        map.put(new Character('7'), new Integer(7));
        map.put(new Character('8'), new Integer(8));
        map.put(new Character('9'), new Integer(9));
        for(char num: s.toCharArray()){
            if(map.containsKey(num)) {
                value += map.get(num) * Math.pow(10, total_length);
                total_length--;
            }
            else{
                return -9999; //error value
            }

        }

        return value;



    }



    public static void main(String[] args) {
        new GlobalScreen();
    }


}

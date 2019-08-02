import com.mongodb.connection.SocketSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Test {
    JFrame frame;
    JButton [] places;

    Socket connection;
    DataOutputStream output;
    DataInputStream input;
    Scanner Keyboard = new Scanner(System.in);
    MouseListener mouseListener;


    public Test(){
        frame = new JFrame("Test");
        frame.setVisible(true);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createPlaces();
        try{
            connection = new Socket("localhost", 666);
            System.out.println("Accepted connection@ " + connection.getInetAddress());
            output = new DataOutputStream(connection.getOutputStream());
            input = new DataInputStream(connection.getInputStream());
        }
        catch(IOException e){
            e.printStackTrace();
        }

        Reciever r = new Reciever("Reciever thread", input);
        r.start();



    }

    public static void main(String[] args) {
        new Test();
    }
    public void createPlaces(){
        places = new JButton[7];
        JPanel buttonPannel = new JPanel(new GridLayout(1, 7));
        for(int i  = 0; i < places.length; i++){
            places[i] = new JButton();
            places[i].setText("" +i);
            places[i].setFocusable(false);
            System.out.print(i + " ID: " + places[i].hashCode());
            buttonPannel.add(places[i]);
        }
        frame.add(buttonPannel);
    }



    public class Reciever extends Thread{
        private int recieved_col = -999;
        private DataInputStream input;
        private String name;
        private boolean hasRecievedInput = false;
        private NetworkGame game;
        public Reciever(String n, DataInputStream in){
            name = n;
            input = in;
        }

        public void setHasRecievedInput(Boolean hasRecievedInput){
            this.hasRecievedInput = hasRecievedInput;
        }

        @Override
        public void run(){
            System.out.println("IN THE RUN METHOD OF RECIEVER");
            while(true){
                if(!hasRecievedInput){
                    try{
                        recieved_col = input.readInt();
                        System.out.println(recieved_col);
                        hasRecievedInput = true;
                        game.disableDropPanel();
                        //update the game board
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }

                }
                else{
                    try{
                        this.sleep(5);
                        hasRecievedInput = false;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }


    }
}

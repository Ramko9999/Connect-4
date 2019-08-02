import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class NetworkGame extends Game {
    private boolean isClient;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean move = true;
    private Sender sender;
    private Reciever reciever;


    public NetworkGame(Document d, boolean move, boolean c, DataInputStream in, DataOutputStream out) {
        super(null, move);
        isClient = c;
        input = in;
        output = out;
        playerRed = true;
        listen_to_network(output, input);

    }

    public void drops(){
        for(int i = 0; i < drops.length; i++){
            int col = i;
            drops[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sender.setHasMoved(true);
                    sender.setCol(col);
                    JOptionPane.showMessageDialog(null, "You clicked a button");
                }
            });
        }
    }


    public void listen_to_network(DataOutputStream output, DataInputStream input) {
        int x, y;
        String file, otherFile;
        boolean hasRecievedInput, hasMoved;
        //Client will move first for now
        if (isClient) {
            hasMoved = false;
            x = 1;
            y = 2;
            file = "red.png";
            otherFile = "yellow.png";

        } else {
            hasMoved = false;
            x = 2;
            y = 1;
            file = "yellow.png";
            otherFile = "red.png";
        }
        sender = new Sender("Sender Thread", output, hasMoved);
        sender.start();
        //reciever = new Reciever("Reciever Thread", input, this, hasRecievedInput);
        //reciever.start();
        drops();

    }
    public class Sender extends Thread{
        private String name;
        private boolean hasMoved = false;
        private int col_move = -999;
        private DataOutputStream output;
        public Sender(String n, DataOutputStream outputStream, boolean hasMoved){
            name = n;
            output = outputStream;
        }

        public void setHasMoved(boolean m){
            hasMoved = m;
        }

        public void setCol(int col){
            col_move = col;
        }

        @Override
        public void run(){
        System.out.println("IN THE RUN METHOD OF SENDER");
            while(true){
                System.out.println("I am inside while loop");
                if(hasMoved){

                    try{
                        System.out.println("ATTEMPTING TO SEND MOVE...");
                        output.writeInt(col_move);
                        output.flush();
                        System.out.println("Sent the move");
                        hasMoved = false;
                        col_move = -999;
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    public class Reciever extends Thread{
        private int recieved_col = -999;
        private DataInputStream input;
        private String name;
        private boolean hasRecievedInput;
        private NetworkGame game;
        public Reciever(String n, DataInputStream in, NetworkGame game, boolean hasRecievedInput){
            name = n;
            input = in;
            this.game = game;
            this.hasRecievedInput = hasRecievedInput;
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
                    if(game.sender.hasMoved == true){
                        hasRecievedInput = false;
                        game.enableDropPanel();
                    }
                    else{

                    }
                }
            }
        }


    }




}

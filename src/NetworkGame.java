import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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

    private int yourMoveNum;
    public int opponentMoveNum;

    public String yourMoveChipURL;
    public String opponentMoveChipURL;

    public JLabel moveCaller;


    public NetworkGame(Document d, boolean move, boolean c, DataInputStream in, DataOutputStream out) {
        super(null, move);
        moveCaller = new JLabel("");
        moveCaller.setForeground(Color.WHITE);
        moveCaller.setFont(new Font("Press Start 2P", 0, 16));

        this.add(moveCaller, BorderLayout.SOUTH);
        isClient = c;
        input = in;
        output = out;
        playerRed = true;
        listen_to_network(output, input);
    }

    public void startUpCursor(String file) { //this is how we can get the cursor to change
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL locale = this.getClass().getResource(file);
        try {
            BufferedImage chip = ImageIO.read(locale);
            mouse = toolkit.createCustomCursor(chip, new Point(30, 30), "coin");
            this.setCursor(mouse);
        } catch (IOException e1) {
            System.out.println("Line 99" + e1);
        }

    }

    public void listen_to_network(DataOutputStream output, DataInputStream input) {
        //Client will move first for now
        if (isClient) {
            yourMoveNum = 1;
            opponentMoveNum = 2;
            yourMoveChipURL = "red.png";
            opponentMoveChipURL = "yellow.png";
            moveCaller.setText("YOUR MOVE");
            moveCaller.setForeground(Color.GREEN);
            startUpCursor(yourMoveChipURL);
        } else {
            yourMoveNum = 2;
            opponentMoveNum = 1;
            yourMoveChipURL = "yellow.png";
            opponentMoveChipURL= "red.png";
            moveCaller.setText("THEIR MOVE");
            moveCaller.setForeground(Color.RED);
            startUpCursor(yourMoveChipURL);

        }

        listen_for_action();
        GroupedRecievers gr = new GroupedRecievers("Group of Recievers", input, this);
        gr.start();
    }

    public void listen_for_action(){
        for(int i = 0; i < drops.length; i++){
            int col = i;
            drops[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(moveCaller.getText().equals("THEIR MOVE")){
                        JOptionPane.showMessageDialog(null, "NOT YOUR MOVE");
                    }
                    else{
                        int row = BottomDrop(gameDrops, col);
                        if(row < 0){
                            JOptionPane.showMessageDialog(null, "Column is full, try again");
                        }
                        else{
                            gameDrops[row][col] = yourMoveNum;
                            populateChips("red.png", "yellow.png");
                            Sender s = new Sender("Sender Thread", col,  output);
                            s.start();
                            if (gameWinner(gameDrops, row, col) > 0) {
                                disableDropPanel();
                                if (gameWinner(gameDrops, row, col) == 1) {
                                    JOptionPane.showMessageDialog(null, "Red Wins");

                                } else {
                                    JOptionPane.showMessageDialog(null, "Yellow Wins");
                                }
                            }
                            moveCaller.setText("THEIR MOVE");
                            moveCaller.setForeground(Color.RED);
                        }
                    }

                }
            });
        }
    }




    public String stringify(int [][] array){
        String result = "";
        for(int [] row: array){
            for(int col: row){
                result+=col;
            }
            result+="-";
        }
        return result;
    }

    public int[][] turnToArray(String s){
        int [][] array = new int[gameDrops.length][gameDrops[0].length];
        int row = 0;
        int col = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '-'){
                row++;
                col = 0;
            }
            else{
                switch (s.charAt(i)){
                    case '1':
                        array[row][col] = 1;
                        col++;
                        break;

                    case '2':
                        array[row][col] = 2;
                        col++;
                        break;

                    default:
                        array[row][col] = 0;
                        col++;
                }
            }
        }
        return array;
    }


    public void populateChips(String fileName, String otherFile){
        URL red_chip = this.getClass().getResource(fileName); //finds the images in the folder
        URL yello_chip = this.getClass().getResource(otherFile);
        try {
            BufferedImage red = ImageIO.read(red_chip);  //sets the images to the Jbuttons in the game board
            BufferedImage yellow = ImageIO.read(yello_chip);
            Icon red_tile = new ImageIcon(red); Icon yellow_tile = new ImageIcon(yellow);
            for(int row = 0 ; row < gameDrops.length; row++){
                for(int col = 0; col < gameDrops[row].length; col++){
                    switch (gameDrops[row][col]){
                        case 1:
                            places[row][col].setIcon(red_tile);
                            break;
                        case 2:
                            places[row][col].setIcon(yellow_tile);
                        default:
                            continue;
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();

        }
    }

    public void threadInfo(){
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for(Thread t: threads){
            System.out.println(t.getName() + " " + t.getState());
        }
    }

}

class Sender extends Thread{
    private String name;
    public boolean click = false;
    private DataOutputStream output;
    private int col;
    public Sender(String name, int col, DataOutputStream output){
        this.name = name;
        this.col = col;
        this.output = output;
    }
    @Override
    public void run(){
        System.out.println(this.col);
        try{
            this.output.writeInt(this.col);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


}


class GroupedRecievers extends Thread{
    private String name;
    private DataInputStream input;
    private NetworkGame game;
    public GroupedRecievers(String name, DataInputStream input, NetworkGame g){
        this.name = name;
        this.input = input;
        this.game = g;

    }

    @Override
    public void run(){
        while(true){
            Reciever r = new Reciever(this.input, this.game);
            r.run();
            r = null;
        }
    }
}

class Reciever extends Thread{
    private DataInputStream input;
    private NetworkGame game;
    public Reciever(DataInputStream input, NetworkGame g){
        this.input = input;
        this.game = g;
    }

    //alter the board itself in reciever, so I must get some of the methods from game


    @Override
    public void run(){
        try{
            System.out.println("-------------------------\n Looking for Move...");
            int col = input.readInt();
            System.out.println("Recieved Move : " + col);
            int row = game.BottomDrop(game.gameDrops, col);
            game.gameDrops[row][col] = game.opponentMoveNum;
            game.populateChips("red.png", "yellow.png");
            game.moveCaller.setText("YOUR MOVE");
            game.moveCaller.setForeground(Color.GREEN);
            if (game.gameWinner(game.gameDrops, row, col) > 0) {
                game.disableDropPanel();
                if (game.gameWinner(game.gameDrops, row, col) == 1) {
                    JOptionPane.showMessageDialog(null, "Red Wins");

                } else {
                    JOptionPane.showMessageDialog(null, "Yellow Wins");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}




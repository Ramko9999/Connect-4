import org.bson.Document;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class OldGame extends Game {


    public OldGame(Document d){
        super(d, true); //dummy super
        this.playerRed = ((int) d.get("Recent Color") == 1);
        this.gameDrops = convert((String) d.get("Recent Game"));
        fill_out_board();
    }

    public int [][] convert(String s) {
        int[][] array = new int[6][7]; //hardcoded since there is a small possibility of expanding
        int i = 0;
        int j = 0;
        for (int c = 0; c < s.length(); c++) {
            if (s.charAt(c) == '-') { // if the char is a '-', then we can simply skip it
                continue;
            }

            if (j > 6) {

                i++;
                if (i > 5) {
                    break;
                }
                j = 0;
            }
            switch (s.charAt(c)) {

                case '1':
                    array[i][j] = 1;
                    j++;
                    break;


                case '2':
                    array[i][j] = 2;
                    j++;
                    break;

                default:
                    array[i][j] = 0;
                    j++;
                    break;


            }
        }
        return array;
    }

    public void fill_out_board(){
       URL red = this.getClass().getResource("red.png");
       URL yellow = this.getClass().getResource("yellow.png");


        int redChips = 0;
        int yellowChips = 0;
        try{
            ImageIcon R = new ImageIcon(ImageIO.read(red));
            ImageIcon Y = new ImageIcon(ImageIO.read(yellow));
            for(int i = 0; i < places.length; i++){
                for(int j = 0; j < places[0].length; j++){

                    switch(gameDrops[i][j]){
                        case 1:
                            places[i][j].setIcon(R);
                            redChips++;
                            break;

                        case 2:
                            places[i][j].setIcon(Y);
                            yellowChips++;
                            break;

                        default:
                            //do nothing
                            break;
                    }

                }
            }
        }
        catch(Exception e1){

        }
        switch ((int) userFile.get("Recent Color")){

            case 1:
                System.out.println("redChips - yellowChips = " +  (redChips - yellowChips) );

                switch (redChips - yellowChips){

                    case 1:
                        playerRed = false;
                        break;
                    default:
                        playerRed = true;
                        break;

                }
            case 2:

                System.out.println("yellowChips - redChips = " +  -1* (redChips - yellowChips));
                switch (yellowChips - redChips){

                    case 1:
                        playerRed = true;
                        break;
                    default:
                        playerRed = false;
                        break;

                }

        }
    }



}

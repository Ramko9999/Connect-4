import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ComputerGame extends Game{

    JButton computerButton = new JButton("Click for Computer Move");
    public ComputerGame(Document d, boolean first){
        super(d, first);
        this.setSize(612, 700);
        this.addComputerPanel();
        this.listenForDrops();
        this.clickForComputer();
    }
    public void addComputerPanel(){
        JPanel computerPanel = new JPanel();
        computerPanel.add(computerButton);
        this.add(computerPanel, BorderLayout.SOUTH);
    }

    public void clickForComputer(){
        computerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Fired");
                int player = 0;
                String [] files = { "yellow.png", "red.png"};
                if(playerRed){
                    player = 1;
                }
                else{
                    player = 2;
                }
                System.out.println("Player is " + player);
                //get the best move based on score
                HashMap<String, Integer> bestMove = new HashMap<>();
                int [] scores = new int[7];
                bestMove.put("column" , -1);
                bestMove.put("row" , -1);
                bestMove.put("score", -1);
                for(int i = 0; i < 7; i++){
                    //automatically disregrad moves that are illegal
                    if(!(BottomDrop(copyArray(gameDrops), i) < 0)){

                        Move potentialMove = new Move(player, copyArray(gameDrops), i);
                        System.out.println(potentialMove.col + " : "  + potentialMove.score);
                        scores[i] = potentialMove.score;

                        if(bestMove.get("score") < potentialMove.score){
                            bestMove = new HashMap();
                            bestMove.put("column" , i);
                            bestMove.put("row", potentialMove.row);
                            bestMove.put("score", potentialMove.score);
                        }
                    }

                }



                int col = bestMove.get("column");
                int row = bestMove.get("row");

                //update GUI and gameboard
                setMoveAndChangeCursor(files[player % 2], files[(player + 1) % 2], col, row);
                gameDrops[row][col] = player;
                playerRed = !playerRed;
                displayArray(gameDrops);
                //check for a win
                if (gameWinner(gameDrops, row, col) > 0) {
                    disableDropPanel();
                    if (gameWinner(gameDrops, row, col) == 1) {
                        JOptionPane.showMessageDialog(null, "Red Wins");

                    } else {
                        JOptionPane.showMessageDialog(null, "Yellow Wins");
                    }
                }
            }
        });

        }

        public static void main(String [] args){
            new ComputerGame(null, true);
        }






}

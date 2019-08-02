import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Game extends JFrame{
    JButton[][] places = new JButton[6][7];
    int[][] gameDrops;
    JButton[] drops = new JButton[7];
    boolean playerRed = false;
    Cursor mouse;
    Document userFile = null;
    Registrar r = new Registrar();


    public Game(Document d, Boolean first){
        super("Game");
        this.setVisible(true);
        this.setSize(612,618);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        userFile = d;
        playerRed = first;
        createDropPanel();
        createButtonPanel();
        startUpCursor();
        gameDrops = new int[6][7];
        ResetPanel resetPanel = new ResetPanel();
        this.add(resetPanel.Main, BorderLayout.SOUTH);
        resetPanel.listen_for_action(this);
    }




    public void reset(){
        this.dispose();
        new Game(userFile, playerRed).listenForDrops();
    }


    public void createDropPanel() {
        JPanel panelForDrops = new JPanel();
        panelForDrops.setBackground(Color.BLACK);
        for (int i = 0; i < 7; i++) {
            JButton temp = new JButton("drop  ");
            //sets the characteristics for the game
            temp.setOpaque(true);
            temp.setContentAreaFilled(false); //makes the buttons invisible
            temp.setBorderPainted(true);
            //temp.setForeground(Color.BLACK);
            panelForDrops.add(temp);
            drops[i] = temp;

        }
        this.add(panelForDrops, BorderLayout.NORTH);
    }


    public void createButtonPanel() {
        JPanel panelForButtons = new JPanel();
        panelForButtons.setBackground(Color.BLACK);
        panelForButtons.setLayout(new GridLayout(6, 7));
        for (int i = 0; i < places.length; i++) {
            for (int j = 0; j < places[0].length; j++) {
                    JButton temp = new JButton();
                    panelForButtons.add(temp);
                    places[i][j] = temp;
            }
        }

        this.add(panelForButtons);

    }

    public void startUpCursor() { //this is how we can get the cursor to change
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL locale = null;
        System.out.println(playerRed);
        if(playerRed) {
            locale = this.getClass().getResource("red.png");
        }
        else{
            locale = this.getClass().getResource("yellow.png");
        }
        try {
            BufferedImage chip = ImageIO.read(locale);
            mouse = toolkit.createCustomCursor(chip, new Point(30, 30), "coin");
            this.setCursor(mouse);
        } catch (IOException e1) {
            System.out.println("Line 99" + e1);
        }

    }

    public void setMoveAndChangeCursor(String firstFileName, String secondFileName, int col, int row) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL locale = this.getClass().getResource(firstFileName); //finds the images in the folder
        URL nextTurn = this.getClass().getResource(secondFileName);

        try {
            BufferedImage chip = ImageIO.read(locale); //sets the images to the Jbuttons in the game board
            Icon tile = new ImageIcon(chip);
            places[row][col].setIcon(tile);
            BufferedImage next = ImageIO.read(nextTurn);
            mouse = toolkit.createCustomCursor(next, new Point(30, 30), "chip");
            this.setCursor(mouse);

        } catch (IOException e1) {
            System.err.println("Images aren't working");
        }

    }

    public void listenForDrops() {
        for (int i = 0; i < drops.length; i++) { //drops is the array containing the drop buttons
            final int col = i;

            drops[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //inital condition that makes sure the move is valid
                    if (BottomDrop(gameDrops, col) < 0) {
                        JOptionPane.showMessageDialog(null, "This column is full, try another one");

                    } else {

                        int row = BottomDrop(gameDrops, col);

                        if (playerRed) {

                            //if its red's turn, it will put a red image in the tile

                            setMoveAndChangeCursor("red.png", "yellow.png", col, row);
                            //print out the score of the game
                            gameDrops[BottomDrop(gameDrops, col)][col] = 1;


                            if(userFile != null) {

                                Document d = (Document) r.collection.find(userFile).first();
                                if(d != null) {
                                    d.replace("Recent Game", unWrap(gameDrops));
                                    System.out.println(unWrap(gameDrops));
                                    r.collection.replaceOne(userFile, d);
                                    userFile = d;
                                }
                            }
                            playerRed = false;

                            displayArray(gameDrops);

                        } else {

                            //changes the tile to yellow and execute the same thing it did to red
                            setMoveAndChangeCursor("yellow.png", "red.png", col, row);
                            gameDrops[BottomDrop(gameDrops, col)][col] = 2;

                            //check whether there is a logged in account
                            if(userFile != null) {
                                Document d = (Document) r.collection.find(userFile).first();
                                if(d != null) {
                                    d.replace("Recent Game", unWrap(gameDrops));
                                    System.out.println(unWrap(gameDrops));
                                    r.collection.replaceOne(userFile, d);
                                    userFile = d;
                                }
                            }

                            playerRed = true;

                            displayArray(gameDrops);
                        }
                        if (gameWinner(gameDrops, row, col) > 0) {
                            disableDropPanel();
                            if (gameWinner(gameDrops, row, col) == 1) {
                                JOptionPane.showMessageDialog(null, "Red Wins");

                            } else {
                                JOptionPane.showMessageDialog(null, "Yellow Wins");
                            }
                        }


                    }

                }
            });
        }

    }
    public int BottomDrop(int[][] g, int col) { //g = the 2d array of the game, and col = designated column
        int row = -1;
        for (int i = 0; i < g.length; i++) {
            if (g[i][col] == 0) { //keeps moving the column until row != 0
                row = i;
            } else {
                break; //Sorry for this ugly break :)
            }
        }
        return row; //if row == -1, that means the column is full
    }

    public String unWrap(char[] c) { //used to unWrap the char [] array password
        String s = "";
        for (char character : c) {
            s += character;
        }
        return s;
    }

    public String unWrap(int [][] array){ //unwrapping the array into string format
        String game = "";
        for(int [] row: array){
            for ( int value : row){
                switch (value){
                    case 1:
                        game += "1";
                        break;
                    case 2:
                        game += "2";
                        break;
                    default:
                        game+= "0";
                        break;
                }
            }
            game+= "-";
        }
        return game;
    }

    public void displayArray(int[][] a) {
        for (int[] row : a) {
            for (int x : row) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void disableDropPanel(){
        for(JButton d: drops){
            d.setEnabled(false);
        }
    }

    public void enableDropPanel(){
        for(JButton d: drops){
            d.setEnabled(true);
        }
    }

    //below lies all the game checking methods



    public int gameWinner(int[][] g, int row, int col) {
        if (gameWinRow(g, row, col) > 0) {
            return gameWinRow(g, row, col);
        } else if (gameWinCol(g) > 0) {
            return gameWinCol(g);
        } else if (gameWinDiag(g, row, col) > 0) {
            return gameWinDiag(g, row, col);
        }
        return -1;
    }
    public int gameWinRow(int[][] g, int row, int col) {
        int counter = 0;

        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length - 1; j++) {

                if (g[i][j] != 0) {
                    if (g[i][j] == g[i][j + 1]) {
                        counter++;
                    } else {
                        counter = 0;
                    }
                } else {
                    counter = 0;
                }
                if (counter == 3) {
                    return g[i][j];
                }
            }
            if (counter == 3) {
                return g[i][g[0].length];
            }
            counter = 0;
        }
        return -1;
    }
    public int gameWinCol(int[][] g) {
        int counter = 0;
        for (int j = 0; j < g[0].length; j++) {
            for (int i = 0; i < g.length - 1; i++) {

                if (g[i][j] != 0) {
                    if (g[i][j] == g[i + 1][j]) {
                        counter++;
                    } else {
                        counter = 0;
                    }
                } else {
                    counter = 0;
                }
                if (counter == 3) {
                    return g[i][j];
                }
            }
            counter = 0;
        }
        return -1;
    }
    public int gameWinDiag(int[][] g, int row, int col) {
        //GridChecker will create the diagonals for a point in the board
        GridChecker checker = new GridChecker(g);
        ArrayList<XPoint> possibleSquares = checker.getPointList(row, col);
        if (possibleSquares.size() == 0) {
            return -1;
        }
        //split the list into 2 parts
        ArrayList<XPoint> fHalf = new ArrayList<>();
        ArrayList<XPoint> sHalf = new ArrayList<>();
        boolean secondList = false;
        for (int i = 0; i < possibleSquares.size(); i++) {
            if (secondList) {
                sHalf.add(possibleSquares.get(i));
            } else if (possibleSquares.get(i).equals(new XPoint(100, 100))) { //shifts the additions to the second list
                secondList = true;
            } else {
                fHalf.add(possibleSquares.get(i));
            }
        }

        //checking whether a game has been won in a diagonal
        int counter = 0;
        for (int i = 0; i < fHalf.size() - 1; i++) {
            int currentR = fHalf.get(i).getRow(); //declaring the rows and columns I have to check
            int currentC = fHalf.get(i).getCol();

            int futureR = fHalf.get(i + 1).getRow();
            int futureC = fHalf.get(i + 1).getCol();

            if (g[currentR][currentC] == g[futureR][futureC] && g[currentR][currentC] != 0) {
                counter++;
                if (counter == 3) {
                    return g[row][col];
                }
            } else {
                counter = 0;
            }
        }

        counter = 0;
        if (sHalf.size() == 0) {
            return -1;
        }

        for (int i = 0; i < sHalf.size() - 1; i++) {
            int currentR = sHalf.get(i).getRow(); //declaring the rows and columns I have to check
            int currentC = sHalf.get(i).getCol();
            int futureR = sHalf.get(i + 1).getRow();
            int futureC = sHalf.get(i + 1).getCol();

            if (g[currentR][currentC] == g[futureR][futureC] && g[currentR][currentC] != 0) {
                counter++;
                if (counter == 3) {
                    return g[row][col];
                }
            } else {
                counter = 0;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        new Game(null, true);
    }


}

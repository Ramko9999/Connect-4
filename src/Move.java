import java.util.ArrayList;

public class Move {
    int player;
    int [][] gameArray;
    int col;
    int row;
    int score;

    public Move(int p, int [][] gameArray, int col){
        this.player = p;
        this.gameArray = gameArray;
        this.col = col;
        this.row = BottomDrop(col, gameArray);
        gameArray[row][this.col] = this.player;
        this.score = 0;
        evaluate();
        //evaluate the move
    }

    //this shall set the score of a move
    public void evaluate(){
        //check the position of the move, moves in the middle are much more powerful than random moves else where
        this.score += positionWeighter();
        //check whether a potential attack of the player can be built
        this.score += buildThree();
        //check whether a potential attack of the opposition is being built up
        this.score += stopThree();
        //check whether the current move will stop a potential game winner from the opposition
        this.score += stopGameWinner();
        //check whether the move presents a game winner for the player
        this.score = Math.max(this.score, gameWinner());

        System.out.println("For "  + this.player + " the score for this move is " + this.score);

    }

    //check whether the move can build a potential attack
    public int buildThree(){
        int [] potentialMoves = {col-3, col - 2, col - 1, col, col + 1, col + 2, col + 3};
        for(int i = 0; i < potentialMoves.length; i++){
            if(potentialMoves[i]  >= 0 && potentialMoves[i] < 7){
                int [][] oneMoveSimulatedArray = copyArray(gameArray);
                int potentialRow = BottomDrop(potentialMoves[i], oneMoveSimulatedArray);
                if(potentialRow != -1){
                    oneMoveSimulatedArray[potentialRow][potentialMoves[i]] = this.player;
                    if(gameWinRow(oneMoveSimulatedArray, row, col ) > 0){
                        return 200;
                    }
                    if(gameWinCol(oneMoveSimulatedArray) > 0){
                        return 150;
                    }
                    if(gameWinDiag(gameArray, row, col) > 0){
                        return 200;
                    }
                }
            }
        }
        return 0;
    }

    //check for a potential attack 2 moves ahead
    public int stopThree(){
        int [][] simulatedMoves = copyArray(gameArray);
        simulatedMoves[row][col] = 2 - ((this.player + 1) % 2);
        int [] potentialMoves = {col-3, col - 2, col - 1, col, col + 1, col + 2, col + 3};
        for(int i = 0; i < potentialMoves.length; i++){
            if(potentialMoves[i] >= 0 && potentialMoves[i] < 7){
                //create another simulated array and make moves, if any moves give a game winning move, then give points
                int [][] twoMoveSimulatedArray = copyArray(simulatedMoves);
                int potentialRow = BottomDrop(potentialMoves[i], twoMoveSimulatedArray);
                if(potentialRow != -1){
                    twoMoveSimulatedArray[potentialRow][potentialMoves[i]] = 2 - ((this.player + 1) % 2);
                    if(gameWinRow(twoMoveSimulatedArray, row, col ) > 0){
                        return 100;
                    }
                    if(gameWinCol(twoMoveSimulatedArray) > 0){
                        return 75;
                    }
                    if(gameWinDiag(twoMoveSimulatedArray, row, col ) > 0){
                        return 100;
                    }
                }

            }
        }
        return 0;

    }


    //utility methods

    public int BottomDrop(int col, int [][] gameArray) { //g = the 2d array of the game, and col = designated column
        int row = -1;
        for (int i = 0; i < gameArray.length; i++) {
            if (this.gameArray[i][col] == 0) { //keeps moving the column until row != 0
                row = i;
            } else {
                break; //Sorry for this ugly break :)
            }
        }
        return row; //if row == -1, that means the column is full
    }

    public int[][] copyArray(int [][] array){
        int [][] newArray = new int[array.length][];
        for(int i = 0; i < array.length; i++){
            newArray[i] = array[i].clone();
        }
        return newArray;
    }

    public int positionWeighter(){
        int [] columnWeighter = {1, 3, 4, 6, 4, 3, 1};
        int [] rowWeighter = {2, 2, 3, 4, 4, 2};
        return columnWeighter[col] * rowWeighter[row];
    }

    //used to evaluate whether a potential game winning move is stopped from the opposition
    public int stopGameWinner(){
        //copy game array
        int [][] simulationArray = copyArray(this.gameArray);
        /*changing the player to the opposition and checking whether if the player's move was replaced with the opposition, a game winning move would have occured
        If it does occur, then give 600 points, as the player just stopped a potential game winning move.
         */

        simulationArray[row][col] = 2 - ((this.player + 1) % 2);
        if(gameWinRow(simulationArray, row, col) > 0){
            return 600;
        }
        if(gameWinCol(simulationArray) > 0){
            return 600;
        }
        if(gameWinDiag(simulationArray, row, col) > 0){
            return 600;
        }
        return 0;

    }


    //used to evaluate a potential game winning move from the player's perspective, if the move is a check mate then that move should give a score of 1000
    public int gameWinner() {
        if (gameWinRow(this.gameArray, this.row, this.col) > 0) {
            return 1000;
        } else if (gameWinCol(this.gameArray) > 0) {
            return 1000;
        } else if (gameWinDiag(this.gameArray, this.row, this.col) > 0) {
            return 1000;
        }
        return 0;
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
}

import java.util.ArrayList;

public class MiniMaxTree {
    Node parentMoveNode;
    int depth;
    int whoMoved;

    public MiniMaxTree(int depth, Node moveNode){
        this.parentMoveNode = moveNode;
        this.depth = depth;
        this.whoMoved = this.parentMoveNode.currentMove.player;
        createMinMaxTree();

    }

    public void createMinMaxTree(){
        parentMoveNode = createMinMaxTreeHelper(parentMoveNode, this.depth, 2 - ((parentMoveNode.currentMove.player - 1) % 2));
    }

    public void compareMoves(){
        int score = -1;
        int col = -1;
        for(Node n : this.parentMoveNode.childMoves){
            n.currentMove.score =  findBestPossibleMove(n, this.depth);
            if(score < n.currentMove.score){
                score = n.currentMove.score;
                col = n.currentMove.col;
            }
        }
        System.out.println("Score : " + score + " Col: "+ col);
    }

    public Node createMinMaxTreeHelper(Node parent, int depth, int player){
        if(depth == 0){
            displayArray(parent.currentMove.gameArray);
            return parent;
        }
        parent.childMoves = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            int [][] simulationArray = copyArray(parent.currentMove.gameArray);
            if(BottomDrop(i, simulationArray) > 0){
                Move possibleMove = new Move(player, copyArray(parent.currentMove.gameArray), i);
                Node moveNode = new Node(possibleMove);
                Node validatedMoveNode = createMinMaxTreeHelper(moveNode, depth -1, 2 - ((player - 1) % 2));
                parent.childMoves.add(validatedMoveNode);
            }


        }

        return parent;
    }

    //this should run after the tree has been built
    public int findBestPossibleMove(Node parentMoveNode, int depth){
        if(parentMoveNode.currentMove.score == 1000 || depth == 2){
            return parentMoveNode.currentMove.player == 1 ? parentMoveNode.currentMove.score : parentMoveNode.currentMove.score * -1;
        }
        if(parentMoveNode.currentMove.player != whoMoved){
            int maxScore = -9999;
            for(Node possibleMove: parentMoveNode.childMoves){
                int score = findBestPossibleMove(possibleMove, depth -1);
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        }
        else{
            int minScore = 9999;
            for(Node possibleMove: parentMoveNode.childMoves){
                int score = findBestPossibleMove(possibleMove, depth - 1);
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }

    }

    //utility methods

    public int BottomDrop(int col, int [][] gameArray) { //g = the 2d array of the game, and col = designated column
        int row = -1;
        for (int i = 0; i < gameArray.length; i++) {
            if (gameArray[i][col] == 0) { //keeps moving the column until row != 0
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

    public void displayArray(int[][] a) {
        for (int[] row : a) {
            for (int x : row) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public static void main(String[] args) {
        Move firstMove = new Move(1, new int[6][7], 3);
        Node moveNode = new Node(firstMove);
        MiniMaxTree miniMaxTree = new MiniMaxTree(5, moveNode);
        miniMaxTree.compareMoves();
        System.out.println("-------------------------");
        System.out.println(miniMaxTree.parentMoveNode.childMoves);


    }





}

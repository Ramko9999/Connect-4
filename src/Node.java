import java.util.ArrayList;

public class Node{
    Move currentMove;
    ArrayList<Node> childMoves;

    public Node(Move c){
        this.currentMove = c;
    }

    public void add(ArrayList<Node> c){
        this.childMoves = c;
    }
    
    @Override
    public String toString(){
        return this.currentMove.toString();
    }


}
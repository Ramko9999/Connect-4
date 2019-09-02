import java.util.ArrayList;

public class GridChecker {

    private int[][] gameArray;


    public GridChecker(int[][] a) {

        gameArray = a;
    }

    public ArrayList<XPoint> getPointList(int row, int col) {
        int iRow = row;
        int iCol = col;


        ArrayList<XPoint> firstList = new ArrayList<>();

        while (iRow > 0 && iCol > 0) {
            iRow--;
            iCol--;
        }
        if (iRow >= 3 || iCol >= 4) {

            firstList = new ArrayList<>();

        } else {
            //lets start by appending the downward and right diagonal values our our pointlist

            while (iRow < gameArray.length && iCol < gameArray[0].length) {
                XPoint index = new XPoint(iRow, iCol);
                firstList.add(index);
                iRow++;
                iCol++;

            }
            if (firstList.size() < 4) {
                firstList = new ArrayList<>();
            } else {
                firstList.add(new XPoint(100, 100)); //this a value that will signify that we finished adding one diagonal
            }
        }

        ArrayList<XPoint> secondList = new ArrayList<>();

        iRow = row;
        iCol = col;


        while (iRow > 0 && iCol < gameArray[0].length - 1) {
            iRow--;
            iCol++;

        }

        //lets now add the upward and left diagonal values to our pointList

        while (iRow < gameArray.length && iCol > -1) {
            XPoint index = new XPoint(iRow, iCol);
            secondList.add(index);
            iRow++;
            iCol--;
        }

        if (secondList.size() < 4) {
            secondList = new ArrayList<>();
        }
        ArrayList<XPoint> total = new ArrayList<>();
        total.addAll(firstList);
        total.addAll(secondList);
        return total;


    }

    public static void main(String[] args) { //TESTED & APPROVED 3/1/19
        int[][] a = new int[6][7];
        int r = 5;
        int c = 3;
        GridChecker d = new GridChecker(a);
    }


}

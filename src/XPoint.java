public class XPoint {
    private int row, col;

    public XPoint(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String toString() {
        return row + " " + col;
    }

    public boolean equals(XPoint x) {
        return (x.getRow() == row && x.getCol() == col);
    }


}

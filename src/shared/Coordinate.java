package shared;

public class Coordinate {
    private int positionX;
    private int positionY;

    public Coordinate(int x, int y, int sizeOfBoard){
        if(x>sizeOfBoard-1 || y>sizeOfBoard-1){
            System.err.println("invalid coords");
            return;
        }
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}

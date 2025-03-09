package shared;


import ships.Fleet;

public class Player {

    private int points = 0;
    private String name;
    private Fleet playerFleet;
    private Board playerBoard;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Fleet getPlayerFleet() {
        return playerFleet;
    }

    public void setPlayerFleet(Fleet playerFleet) {
        this.playerFleet = playerFleet;
    }
}

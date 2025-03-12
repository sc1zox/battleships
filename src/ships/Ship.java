package ships;

public class Ship {

    private int length;
    private int health;
    private boolean isAlive = true;
    private boolean isPlaced;
    private Orientation orientation;
    private Types name;


    public Ship(int length, Orientation ori, Types name){
        this.length = length;
        this.health = length;
        this.isPlaced = false;
        this.orientation = ori;
        this.name = name;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public Types getName() {
        return name;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLength() {
        return length;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getHealth() {
        return health;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void hit(){
        this.health--;
        if(this.health==0) {
            this.isAlive = false;
            System.err.println("Ship is destroyed");
        }
    }
}

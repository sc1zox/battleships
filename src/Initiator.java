import shared.Board;
import shared.Player;
import ships.Fleet;

public class Initiator {
    private Player player1;
    private Player player2;

    public Initiator(){
        player1 = new Player();
        player2 = new Player();
    }

    public void init(int sizeOfBoard){
        Board playerBoard1 = new Board(sizeOfBoard);
        Board playerBoard2 = new Board(sizeOfBoard);
        Fleet playerFleet1 = new Fleet();
        Fleet playerFleet2 = new Fleet();
        this.player1.setPlayerBoard(playerBoard1);
        this.player2.setPlayerBoard(playerBoard2);
        this.player1.setPlayerFleet(playerFleet1);
        this.player2.setPlayerFleet(playerFleet2);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}

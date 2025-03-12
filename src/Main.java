
public class Main {
    public static void main(String[] args) {

        Initiator start = new Initiator();
        start.init(8);
        GameEngine game = new GameEngine(start.getPlayer1(), start.getPlayer2(),8);
        game.initializationPhase();
        game.playTurn(); //TODO iwie einen Gamemode passen um die Schifflänge und anzahl zu bestimmen
    }
    }
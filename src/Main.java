

public class Main {
    public static void main(String[] args) {

        Initiator start = new Initiator();
        start.init(8);
        GameEngine game = new GameEngine(start.player1, start.player2,8);
        game.initializationPhase();
        }
    }
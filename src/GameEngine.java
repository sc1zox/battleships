import errors.BombException;
import errors.ShipPlacementException;
import shared.Coordinate;
import shared.LetterToNumber;
import shared.Player;
import ships.Ship;
import ships.Orientation;
import ships.Types;

import java.util.*;

public class GameEngine {

    private Player[] gameTurn;
    private final Scanner scanner = new Scanner(System.in);
    private final int sizeOfBoard;
    private int currentPlayer;
    private final Random random;

    public GameEngine(Player p1, Player p2,int sizeOfBoard){
        this.gameTurn = new Player[2];
        this.gameTurn[0] = p1;
        this.gameTurn[1] = p2;
        this.sizeOfBoard = sizeOfBoard;
        random = new Random();
        determineStartingPlayer();
    }

    public void determineStartingPlayer() {
        currentPlayer = random.nextInt(2);
        System.out.println("Spieler " + (currentPlayer + 1) + " beginnt das Spiel!");
    }

    public Player[] getGameTurn() {
        return gameTurn;
    }

    public void setGameTurn(Player[] gameTurn) {
        this.gameTurn = gameTurn;
    }

    public void initializationPhase() {
        setNamesAndIntroduction();

        placementPhase(currentPlayer);
        System.out.println("All ships placed for Player 1");
        switchPlayer();
        placementPhase(currentPlayer);
        System.out.println("All ships placed for Player 2");
        switchPlayer();
        this.gameTurn[0].getPlayerBoard().displayBoard(); //tmp um map anzuzeigen
        this.gameTurn[1].getPlayerBoard().displayBoard(); //tmp um map anzuzeigen
    }

    private void placementPhase(int turn){
        //TODO replace 1 with lengthOfFleet
        int lengthOfFleet = this.gameTurn[currentPlayer].getPlayerFleet().getShips().keySet().toArray().length;
        for (int i = 0; i < 1; i++) {
            placeShip(turn);
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 0) ? 1 : 0;
        System.out.println("Spielerwechsel: Jetzt ist Spieler " + (currentPlayer + 1) + " am Zug.");
    }

    public void playTurn() {
        System.out.println("Spieler " + (currentPlayer + 1) + " ist am Zug.");
        evaluatePlayerTurn();
    }


    private void setNamesAndIntroduction(){
        System.out.println("Willkommen zum Spiel!");

        System.out.print("Spieler 1, gib deinen Namen ein: ");
        this.gameTurn[0].setName(scanner.nextLine());

        System.out.print("Spieler 2, gib deinen Namen ein: ");
        this.gameTurn[1].setName(scanner.nextLine());

        System.out.println("\nWillkommen, " + this.gameTurn[0].getName() + " und " + this.gameTurn[1].getName() + "!");
        System.out.println("Lasst uns beginnen!");
    }

    private int askPlayerWhatToBomb(int turn,int enemy){
        this.gameTurn[turn].getPlayerBoard().displayBoard();
        System.out.print("\n Spieler: "+this.gameTurn[turn].getName()+ " Bestimme die Koordinaten um zu bombardieren");

        boolean isHit = false;
        Coordinate target;

        while (this.gameTurn[currentPlayer].getPlayerFleet().isAlive() && this.gameTurn[enemy].getPlayerFleet().isAlive()) {
            target = inputCoordinateDialogue();
            try {
                this.gameTurn[turn].getPlayerBoard().markFieldAsBombed(target);
                 isHit = this.gameTurn[enemy].getPlayerBoard().bomb(target); //hier board von person die nicht dran ist
                break; // Erfolgreiche Bombe -> aus der Schleife rausgehen
            } catch (BombException e) {
                System.err.println(e.getMessage());
                System.out.println("\n Bitte wähle eine andere Koordinate.");
            }
        }

        if (isHit) {
            System.out.println("\n Bomb getroffen! Du bist nochmal dran.");
            return turn;
        }
        System.out.println("\n Bomb verfehlt! Nächster Spieler ist dran.");
        return enemy;
    }

    public void evaluatePlayerTurn() {
        while (true) {
            int enemy = (currentPlayer == 0) ? 1 : 0;

            if (!this.gameTurn[currentPlayer].getPlayerFleet().isAlive() ||
                    !this.gameTurn[enemy].getPlayerFleet().isAlive()) {
                System.out.println("Spiel beendet! " + this.gameTurn[enemy].getName() + " hat gewonnen.");
                closeScanner();
                return;
            }

            int nextPlayer = askPlayerWhatToBomb(currentPlayer, enemy);

            System.out.println("Press Ü to display boards");
            if (scanner.nextLine().equals("Ü")) {
                this.gameTurn[0].getPlayerBoard().displayBoard();
                this.gameTurn[1].getPlayerBoard().displayBoard();
            }

            if (nextPlayer != currentPlayer) {
                switchPlayer();
            }
        }
    }

    private void placeShip(int turn) {

        String p1 = this.gameTurn[turn].getName();

        System.out.print(p1 + ", drücke ENTER, um deinen Zug zu starten...");
        scanner.nextLine();

        Map<String, Ship> ship = this.gameTurn[turn].getPlayerFleet().getShips();
        Ship shipToPlace = ship.get(this.choiceShip());
        Coordinate[] startAndEnd = null;

        while (startAndEnd == null) {
            try {
                startAndEnd = getCoordinatesForShipPlacingDialogue(shipToPlace, turn);
                this.gameTurn[turn].getPlayerBoard().positionShip(startAndEnd[0], startAndEnd[1], shipToPlace, shipToPlace.getOrientation());
                shipToPlace.setPlaced(true);
            } catch (ShipPlacementException e) {
                System.err.println(e.getMessage() + "Bitte erneut eingeben: \n");
            }
        }
    }


    private void printShipNames() {
        Types[] shipTypes = Types.values();
        Map<String, Ship> ships = this.gameTurn[currentPlayer].getPlayerFleet().getShips();

        int number = 1;
        for (Types type : shipTypes) {
            if(ships.get(type.name()) == null) {
                System.out.println(number + ". " + type.name());
                number++;
            }
        }
    }


    private Coordinate[] getCoordinatesForShipPlacingDialogue(Ship shipToPlace, int turn) throws ShipPlacementException {
        this.gameTurn[turn].getPlayerBoard().displayBoard();

        System.out.println("Gib die Koordinaten ein, um dein Schiff " + shipToPlace.getName() +
                " zu platzieren. Es hat die Länge: " + shipToPlace.getLength());

        System.out.println("Gib zuerst an, ob das Schiff VERTIKAL(v) oder HORIZONTAL(h) platziert werden soll:");
        Orientation orientation = null;

        while (orientation == null) {
            try {
                String input = scanner.nextLine().trim().toUpperCase();
                if (input.equals("V")){
                    orientation = Orientation.VERTIKAL;
                } else if (input.equals("H")) {
                    orientation = Orientation.HORIZONTAL;
                } else {
                    throw new IllegalArgumentException("Fehler: Falsche Eingabe");
                }
                shipToPlace.setOrientation(orientation);
            } catch (IllegalArgumentException e) {
                System.err.println("Fehler: Ungültige Eingabe. Bitte gib VERTICAL oder HORIZONTAL ein.");
            }
        }
        Coordinate start = null;
        Coordinate end = null;

            start = inputCoordinateDialogue();
            end = calculateEndCoordinate(start, orientation, shipToPlace);

            System.out.println("Du hast die Koordinaten gewählt: Start (" + start.getPositionX() + ", " + start.getPositionY() + ")"+ orientation);

        return new Coordinate[] {
                start,
                end
        };
    }

    private Coordinate inputCoordinateDialogue(){
        int startX;
        int startY;
        Coordinate start;

        while (true) {
            try {
                System.out.print("\n X-Koordinate (A-" + LetterToNumber.getLetterFromNumber(this.sizeOfBoard) + "): ");
                String input = scanner.nextLine().trim().toUpperCase();
                if (input.length() != 1 || input.charAt(0) < 'A' || input.charAt(0) > 'Z') {
                    throw new IllegalArgumentException("Fehler: Bitte einen gültigen Buchstaben A-Z eingeben.");
                }
                startY = LetterToNumber.getNumberFromLetter(input.charAt(0)) - 1;

                System.out.print("\n Y-Koordinate (1-" + this.sizeOfBoard + "): ");
                startX = Integer.parseInt(scanner.nextLine()) - 1;
                if(startX <0 || startX > this.sizeOfBoard){
                    throw new IllegalArgumentException("Fehler: Bitte eine gültige Zahl eingeben.");
                }
                start = new Coordinate(startX, startY, this.sizeOfBoard);

                break;
            } catch (NumberFormatException e) {
                System.err.println("Fehler: Bitte nur Zahlen zwischen 1 und " + this.sizeOfBoard + " eingeben.");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        System.out.println("Du hast die Koordinate gewählt: Start (" + startX + ", " + startY + ")");

        return start;
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < this.sizeOfBoard && y >= 0 && y < this.sizeOfBoard;
    }

    private Coordinate calculateEndCoordinate(Coordinate start, Orientation ori, Ship ship) throws ShipPlacementException {
        if (start == null || ori == null || ship == null) {
            throw new IllegalArgumentException("Fehler: Einer der Parameter ist null.");
        }

        int endX = start.getPositionX();
        int endY = start.getPositionY();

        if (ori == Orientation.VERTIKAL) {
            endX = start.getPositionX() + ship.getLength() - 1;
        } else {
            endY = start.getPositionY() + ship.getLength() - 1;
        }


        if (!isValidCoordinate(endX, endY)) {
            throw new ShipPlacementException("Fehler: Endkoordinate (" + endX + "," + endY + ") liegt außerhalb des Spielfelds!");
        }

        return new Coordinate(endX, endY, this.sizeOfBoard);
    }



    private String choiceShip() {
        System.out.print("drücke ENTER, um dein Schiff zu platzieren...");
        scanner.nextLine();
        String[] ships = this.gameTurn[currentPlayer].getPlayerFleet().getShipNames().toArray(new String[0]);
        Map<String,Ship> shipMap = this.gameTurn[currentPlayer].getPlayerFleet().getShips();

        while (true) {
            System.out.println("\nWähle ein Schiff (1-5). Drücke 6 zum Beenden.");
            printShipNames();

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                //TODO refaktorieren weil hier kein spieler String array exisitiert und hier Types referenziert werden soll ebenso die Bedingung für invalide Eingabe
                if (choice >= 1 && choice <= ships.length) {
                    if(shipMap.get(ships[choice - 1]).isPlaced()){
                        throw new ShipPlacementException("Ship is already placed. Choose another one");
                    }
                    return ships[choice - 1];
                } else if (choice == 6) {
                    System.out.println("Schiffsauswahl abgebrochen.");
                    return "";
                } else {
                    System.err.println("Ungültige Auswahl. Bitte eine Zahl zwischen 1 und " + ships.length + " eingeben.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Fehler: Bitte eine gültige Zahl eingeben.");
            } catch (ShipPlacementException e) {
                System.err.println("Fehler: Das Schiff wurde schon platziertd. Bitte erneut wählen");
            }
        }
    }

    private void closeScanner(){
        this.scanner.close();
    }
}

import errors.BombException;
import errors.ShipPlacementException;
import shared.Coordinate;
import shared.LetterToNumber;
import shared.Player;
import ships.Ship;
import ships.orientation;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GameEngine {

    private Player[] gameTurn;
    private final Scanner scanner = new Scanner(System.in);
    private int sizeOfBoard;

    public GameEngine(Player p1, Player p2,int sizeOfBoard){
        this.gameTurn = new Player[2];
        this.gameTurn[0] = p1;
        this.gameTurn[1] = p2;
        this.sizeOfBoard = sizeOfBoard;
    }


    public Player[] getGameTurn() {
        return gameTurn;
    }

    public void setGameTurn(Player[] gameTurn) {
        this.gameTurn = gameTurn;
    }

    public void initializationPhase() {
        Set<String> f1 = this.gameTurn[0].getPlayerFleet().getShipNames();
        Set<String> f2 = this.gameTurn[1].getPlayerFleet().getShipNames();

        int lengthOfFleet = f1.size();

        setNamesAndIntroduction();

        placementPhase(0,lengthOfFleet);// turn based impl missing
        System.out.println("All ships placed for Player 1");
        placementPhase(1,lengthOfFleet); // turn based impl missing also not really working?
        System.out.println("All ships placed for Player 2");
        this.gameTurn[0].getPlayerBoard().displayBoard(); //tmp um map anzuzeigen
        this.gameTurn[1].getPlayerBoard().displayBoard(); //tmp um map anzuzeigen
    }

    //TODO replace 1 with lengthOfFleet
    private void placementPhase(int turn,int lengthOfFleet){
        for (int i = 0; i < 1; i++) {
            placeShip(turn);
        }
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
        System.out.print("Spieler: "+this.gameTurn[turn].getName()+ " Bestimme die Koordinaten um zu bombardieren");

        boolean isHit;
        Coordinate target;

        while (true) {
            target = inputCoordinateDialogue();
            try {
                this.gameTurn[turn].getPlayerBoard().markFieldAsBombed(target);
                 isHit = this.gameTurn[enemy].getPlayerBoard().bomb(target); //hier board von person die nicht dran ist
                break; // Erfolgreiche Bombe -> aus der Schleife rausgehen
            } catch (BombException e) {
                System.err.println(e.getMessage());
                System.out.println("Bitte wähle eine andere Koordinate.");
            }
        }

        if (isHit) {
            System.out.println("Bomb getroffen! Du bist nochmal dran.");
            return turn;
        }
        System.out.println("Bomb verfehlt! Nächster Spieler ist dran.");
        return enemy;
    }

    public void evaluatePlayerTurn(int turn) {
        int enemy = (turn == 0) ? 1 : 0;

        if (!this.gameTurn[turn].getPlayerFleet().isAlive() || !this.gameTurn[enemy].getPlayerFleet().isAlive()) {
            System.out.println("Spiel beendet! " + this.gameTurn[enemy].getName() + " hat gewonnen.");
            closeScanner();
            return;
        }

        int newTurn = askPlayerWhatToBomb(turn, enemy);

        System.out.println("Press Ü to display boards");
        if(scanner.nextLine().equals("Ü")){
            this.gameTurn[0].getPlayerBoard().displayBoard();
            this.gameTurn[1].getPlayerBoard().displayBoard();
        }

        evaluatePlayerTurn(newTurn);
    }



    private void placeShip(int turn) {

        String p1 = this.gameTurn[turn].getName();

        System.out.print(p1 + ", drücke ENTER, um deinen Zug zu starten...");
        scanner.nextLine();

        Map<String, Ship> ship = this.gameTurn[turn].getPlayerFleet().getShips();
        Ship shipToPlace = ship.get(this.choiceShip(turn));
        Coordinate[] startAndEnd = getCoordinatesForShipPlacingDialogue(shipToPlace, turn);
        try {
            this.gameTurn[turn].getPlayerBoard().positionShip(startAndEnd[0],startAndEnd[1],shipToPlace,shipToPlace.getOrientation());
            shipToPlace.setPlaced(true);
        } catch (ShipPlacementException e) {
            System.err.println(e.getMessage());
            System.err.println("ERROR PLACING GAME ENGINGE TEMP");
        }
    }

    private void printShipNames(){
        Set<String> ships = this.gameTurn[0].getPlayerFleet().getShipNames();
        int number = 1;
        for (String ship : ships) {
            System.out.println(number+". "+ship);
            number++;
        }
    }

    private Coordinate[] getCoordinatesForShipPlacingDialogue(Ship shipToPlace, int turn) {
        this.gameTurn[turn].getPlayerBoard().displayBoard();

        System.out.println("Gib die Koordinaten ein, um dein Schiff " + shipToPlace.getName() +
                " zu platzieren. Es hat die Länge: " + shipToPlace.getLength());

        System.out.println("Gib zuerst an, ob das Schiff VERTICAL oder HORIZONTAL platziert werden soll:");
        orientation orientation = null;

        while (true) {
            try {
                orientation = orientation.valueOf(scanner.nextLine().toUpperCase());
                shipToPlace.setOrientation(orientation);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Fehler: Ungültige Eingabe. Bitte gib VERTICAL oder HORIZONTAL ein.");
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
                System.err.println("Orientation ran into an error");
            }
        }
        Coordinate start = null;
        Coordinate end = null;
        try{
            start = inputCoordinateDialogue();
            end = calculateEndCoordinate(start, orientation, shipToPlace);

            System.out.println("Du hast die Koordinaten gewählt: Start (" + start.getPositionX() + ", " + start.getPositionY() + ") - Ende (" + end.getPositionX() + ", " + end.getPositionY() + ")"+ orientation);

        }catch (ShipPlacementException e) {
            System.err.println(e.getMessage());
            System.out.println("Bitte wähle eine andere Position.");
        } catch (NullPointerException e) {
            System.err.println("End calc method broke pls fix");
        }

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

    private Coordinate calculateEndCoordinate(Coordinate start, orientation ori, Ship ship) throws ShipPlacementException {
        if (start == null || ori == null || ship == null) {
            throw new IllegalArgumentException("Fehler: Einer der Parameter ist null.");
        }

        int endX = start.getPositionX();
        int endY = start.getPositionY();

        if (ori == orientation.HORIZONTAL) {
            endX = start.getPositionX() + ship.getLength() - 1;
        } else {
            endY = start.getPositionY() + ship.getLength() - 1;
        }


        if (!isValidCoordinate(endX, endY)) {
            throw new ShipPlacementException("Fehler: Endkoordinate (" + endX + "," + endY + ") liegt außerhalb des Spielfelds!");
        }

        return new Coordinate(endX, endY, this.sizeOfBoard);
    }



    private String choiceShip(int turn) {
        System.out.print("drücke ENTER, um dein Schiff zu platzieren...");
        scanner.nextLine();
        String[] ships = this.gameTurn[turn].getPlayerFleet().getShipNames().toArray(new String[0]);
        Map<String,Ship> shipMap = this.gameTurn[turn].getPlayerFleet().getShips();

        while (true) {
            System.out.println("\nWähle ein Schiff (1-5). Drücke 6 zum Beenden.");
            printShipNames();

            try {
                int choice = Integer.parseInt(scanner.nextLine());

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

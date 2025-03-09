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

        setNamesAndIntroduction();

        for (String ship: f1){
            placeShip(0); // 0 oder 1 je nachdem wer dran ist
        }
        System.out.println("All ships placed for Player 1");
        this.gameTurn[0].getPlayerBoard().displayBoard();
        closeScanner();
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


    private void placeShip(int turn) {

        String p1 = this.gameTurn[turn].getName();

        System.out.print(p1 + ", drücke ENTER, um deinen Zug zu starten...");
        scanner.nextLine();

        Map<String, Ship> ship = this.gameTurn[turn].getPlayerFleet().getShips();
        Ship shipToPlace = ship.get(this.choiceShip(turn));
        Coordinate[] startAndEnd = getCoordinatesDialogue(shipToPlace, turn);
        try {
            this.gameTurn[0].getPlayerBoard().positionShip(startAndEnd[0],startAndEnd[1],shipToPlace,shipToPlace.getOrientation());
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

    private Coordinate[] getCoordinatesDialogue(Ship shipToPlace, int turn) {
        this.gameTurn[turn].getPlayerBoard().displayBoard();

        System.out.println("Gib die Start- und End-Koordinaten ein, um dein Schiff " + shipToPlace.getName() +
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
            }
        }
        int startX, startY;
        Coordinate start,end;

        while (true) {
            try {
                System.out.print("Start X-Koordinate (A-" + LetterToNumber.getLetterFromNumber(this.sizeOfBoard) + "): ");
                String input = scanner.nextLine().trim().toUpperCase();
                if (input.length() != 1 || input.charAt(0) < 'A' || input.charAt(0) > 'Z') {
                    throw new IllegalArgumentException("Fehler: Bitte einen gültigen Buchstaben A-Z eingeben.");
                }
                startY = LetterToNumber.getNumberFromLetter(input.charAt(0)) - 1;

                System.out.print("Start Y-Koordinate (1-" + this.sizeOfBoard + "): ");
                startX = Integer.parseInt(scanner.nextLine()) - 1;
                if(startX <0 || startX > this.sizeOfBoard){
                    throw new IllegalArgumentException("Fehler: Bitte eine gültige Zahl eingeben.");
                }
                start = new Coordinate(startX, startY, this.sizeOfBoard);
                end = calculateEndCoordinate(start, orientation, shipToPlace, turn);

                break;
            } catch (NumberFormatException e) {
                System.err.println("Fehler: Bitte nur Zahlen zwischen 1 und " + this.sizeOfBoard + " eingeben.");
            } catch (ShipPlacementException e) {
                System.err.println(e.getMessage());
                System.out.println("Bitte wähle eine andere Position.");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        System.out.println("Du hast die Koordinaten gewählt: Start (" + startX + ", " + startY + ") - Ende (" + end.getPositionX() + ", " + end.getPositionY() + ")"+ orientation);

        return new Coordinate[]{
                start,
                end
        };
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < this.sizeOfBoard && y >= 0 && y < this.sizeOfBoard;
    }

    private Coordinate calculateEndCoordinate(Coordinate start, orientation ori, Ship ship, int turn) throws ShipPlacementException {
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

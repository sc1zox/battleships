package shared;
import errors.BombException;
import errors.ShipPlacementException;
import ships.Ship;
import ships.Orientation;

public class Board {

    private final Field[][] playerBoard;

    public Board(int sizeOfBoard){
        playerBoard = new Field[sizeOfBoard][sizeOfBoard];
        for (int i = 0; i < this.playerBoard.length; i++) {
            for (int j = 0; j < this.playerBoard.length; j++) {
                this.playerBoard[i][j] = new Field();
            }
        }
    }

    public void displayBoard() {
        System.out.println("   A B C D E F G H");
        System.out.println("  -----------------");

        for (int i = 0; i < playerBoard.length; i++) {
            System.out.print(i+1 + " |");

            for (int j = 0; j < playerBoard[i].length; j++) {
                if (playerBoard[i][j].getWasBombed()) {
                    System.out.print(" X");
                } else if (playerBoard[i][j].isShip()) {
                    System.out.print(" O");
                } else if (playerBoard[i][j].isShip() && playerBoard[i][j].getWasBombed()) {
                    System.out.println(" ⨂");
                } else {
                    System.out.print(" .");
                }
            }
            System.out.println(" |");
        }

        System.out.println("  -----------------");
    }


    public void printBoard(){
        for (int i = 0; i < this.playerBoard.length; i++) {
            for (int j = 0; j < this.playerBoard.length; j++) {
                System.out.println("Position X: "+i+" "+"Position Y: "+j+"Board: "+this.playerBoard[i][j].isShip());
            }
        }
    }

    public void positionShip(Coordinate positionStart, Coordinate positionEnd, Ship ship, Orientation orientation) throws ShipPlacementException {
        if (!checkPlacement(positionStart, positionEnd, ship)) {
            throw new ShipPlacementException("Ungültige Platzierung für Schiff: " + ship.getName() +
                    " von (" + positionStart.getPositionX() + "," + positionStart.getPositionY() + ") bis (" +
                    positionEnd.getPositionX() + "," + positionEnd.getPositionY() + ")");
        }
        ship.setOrientation(orientation);
        switch (ship.getOrientation()) {
            case HORIZONTAL:
                for (int i = positionStart.getPositionY(); i <= positionEnd.getPositionY(); i++) {
                    this.playerBoard[positionStart.getPositionX()][i].setShip(ship);
                }
                break;

            case VERTIKAL:
                for (int i = positionStart.getPositionX(); i <= positionEnd.getPositionX(); i++) {
                    this.playerBoard[i][positionStart.getPositionY()].setShip(ship);
                }
                break;
        }
    }

    public boolean checkPlacement(Coordinate positionStart, Coordinate positionEnd, Ship ship) throws ShipPlacementException {
        switch (ship.getOrientation()) {
            case VERTIKAL:
                if(positionStart == null || positionEnd == null){
                    return false;
                }
                if (positionStart.getPositionY() != positionEnd.getPositionY()) return false;

                int lengthPlacementHorizontal = positionEnd.getPositionX() - positionStart.getPositionX() + 1;
                if (lengthPlacementHorizontal != ship.getLength() || positionEnd.getPositionX() >= playerBoard.length) {
                    return false;
                }

                for (int i = positionStart.getPositionX(); i <= positionEnd.getPositionX(); i++) {
                    if (this.playerBoard[i][positionStart.getPositionY()].isShip()) {
                        throw new ShipPlacementException("Fehler: Auf Position (" + i + "," +
                                positionStart.getPositionY() + ") befindet sich bereits ein Schiff.");
                    }
                }
                return true;

            case HORIZONTAL:
                if(positionStart == null || positionEnd == null){
                    return false;
                }
                if (positionStart.getPositionX() != positionEnd.getPositionX()) return false;

                int lengthPlacementVertical = positionEnd.getPositionY() - positionStart.getPositionY() + 1;
                if (lengthPlacementVertical != ship.getLength() || positionEnd.getPositionY() >= playerBoard.length) {
                    return false;
                }

                for (int i = positionStart.getPositionY(); i <= positionEnd.getPositionY(); i++) {
                    if (this.playerBoard[positionStart.getPositionX()][i].isShip()) {
                        throw new ShipPlacementException("Fehler: Auf Position (" + positionStart.getPositionX() + "," +
                                i + ") befindet sich bereits ein Schiff.");
                    }
                }
                return true;
        }
        return false;
    }

    public boolean bomb(Coordinate bomb) {
        Field tmp = this.playerBoard[bomb.getPositionX()][bomb.getPositionY()];

        if (tmp.isShip()) {
            tmp.getShip().hit();
            return true;
        }
        return false;
    }
    public Field getFieldFromCoordinate(Coordinate target){
        return this.playerBoard[target.getPositionX()][target.getPositionY()];
    }

    public void markFieldAsBombed(Coordinate bomb) throws BombException{
        Field tmp = this.playerBoard[bomb.getPositionX()][bomb.getPositionY()];

        if (tmp.getWasBombed()) {
            throw new BombException("\n Position (" + bomb.getPositionX() + "," + bomb.getPositionY() + ") wurde bereits bombardiert.");
        }
        tmp.setWasBombed(true);
    }
}

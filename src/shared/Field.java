package shared;

import ships.Ship;

public class Field {

    private Ship ship;
    private boolean wasBombed = false;

    public Ship getShip() {
        return ship;
    }

    public boolean getWasBombed() {
        return wasBombed;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public boolean isShip(){
        return this.ship!=null;
    }

    public void  setWasBombed(boolean wasBombed) {
        this.wasBombed = wasBombed;
    }
}

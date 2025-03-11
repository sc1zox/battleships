package ships;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Fleet {

    private HashMap<String, Ship> ships = new HashMap<>();

    public Fleet() {
        ships.put("Mini", new Ship(2, orientation.VERTICAL,types.MINI));
        ships.put("Yacht", new Ship(3, orientation.VERTICAL,types.YACHT));
        ships.put("Cruiser", new Ship(4, orientation.HORIZONTAL,types.CRUISER));
        ships.put("Warship", new Ship(5, orientation.HORIZONTAL,types.WARSHIP));
        ships.put("Battlecruiser", new Ship(6, orientation.VERTICAL,types.BATTLECRUISER));
    }

    public Map<String, Ship> getShips() {
        return ships;
    }

    public Set<String> getShipNames(){
        return ships.keySet();
    }

    public Ship getShip(String name) {
        return ships.get(name);
    }

    public void addShip(String name, Ship ship) {
        ships.put(name, ship);
    }

    public void removeShip(String name) {
        ships.remove(name);
    }

    public boolean isAlive() {
        for ( Ship ship : ships.values()){
            if (ship.isAlive()){
                return true;
            }
        }
        return false;
    }
}

## TODO:
    - falsche koords bei input wirft keinen richtigen Fehler sondern bei Angabe V und 9 geht der input durch und es wird die Koordinate errechnet. Es sollte vorher abbrechen.
    - zufälliges Platzieren ermöglichen
    - choiceShip muss iwie ein Ship anlegen und der fleet hinzufügen, evtl auch eigene Methode aber wie wird die Länge festegelegt? Funktioniert momentan aber ist hard coded in eigener Methode -> in Gamemode packen


- error

---

Wähle ein Schiff (1-5). Drücke 6 zum Beenden.
1. YACHT
2. CRUISER
3. WARSHIP
4. BATTLECRUISER
   1
   Fehler: Das Schiff wurde schon platziert. Bitte erneut wählen

Wähle ein Schiff (1-5). Drücke 6 zum Beenden.
1. YACHT
2. CRUISER
3. WARSHIP
4. BATTLECRUISER
   2
   Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
   at GameEngine.choiceShip(GameEngine.java:289)
   at GameEngine.placeShip(GameEngine.java:145)
   at GameEngine.placementPhase(GameEngine.java:58)
   at GameEngine.initializationPhase(GameEngine.java:45)
   at Main.main(Main.java:8)

Process finished with exit code 1

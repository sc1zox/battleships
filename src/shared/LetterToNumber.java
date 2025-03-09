package shared;

import java.util.HashMap;
import java.util.Map;

public enum LetterToNumber {
    A(1), B(2), C(3), D(4), E(5), F(6), G(7), H(8), I(9), J(10),
    K(11), L(12), M(13), N(14), O(15), P(16), Q(17), R(18), S(19), T(20),
    U(21), V(22), W(23), X(24), Y(25), Z(26);

    private final int number;
    private static final Map<Character, LetterToNumber> letterMap = new HashMap<>();
    private static final Map<Integer, LetterToNumber> numberMap = new HashMap<>();

    static {
        for (LetterToNumber entry : values()) {
            letterMap.put(entry.name().charAt(0), entry);
            numberMap.put(entry.number, entry);
        }
    }

    LetterToNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public static int getNumberFromLetter(char letter) {
        letter = Character.toUpperCase(letter);
        LetterToNumber entry = letterMap.get(letter);
        if (entry == null) {
            throw new IllegalArgumentException("Ungültiger Buchstabe: " + letter + ". Nur A-Z erlaubt.");
        }
        return entry.getNumber();
    }

    public static char getLetterFromNumber(int number) {
        LetterToNumber entry = numberMap.get(number);
        if (entry == null) {
            throw new IllegalArgumentException("Ungültige Zahl: " + number + ". Nur 1-26 erlaubt.");
        }
        return entry.name().charAt(0);
    }
}

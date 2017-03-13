package team5.game;

/**
 * Created by james on 3/11/17.
 */
public enum TurnType {
    Place, Move;

    @Override
    public String toString() {
        switch (this) {
            case Place: return "place";
            case Move: return "move";
            default: return "ERROR";
        }
    }
}

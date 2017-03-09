package team5.game;

/*
 * Abstract class that holds the basic things that every game logic 
 * should handle to allow for new "pluggable" game logics.
 *
 * @author  Team5
 * @date    2017/03/08
 */
public abstract class GameLogic {

    private String gameName = "";
    public static void testDriverMethod() {
        System.out.println("Abstract Game Logic working");
    }

    public String gameName() { return gameName; }
}

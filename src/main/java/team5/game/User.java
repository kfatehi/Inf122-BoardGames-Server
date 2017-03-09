package team5.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 3/7/17.
 */
public class User {

    private String name = "";
    private List<GameStat> stats = new ArrayList<GameStat>();

    public User(String name) {
        System.out.println("Building User");

        this.name = name;

        for (String gameName : GameLogicFactory.getAllSupportedGames()) {
            stats.add(new GameStat(gameName));
        }
    }

    public String name() { return name; }
    public GameStat gameStat(String gameName) {
        for (GameStat stat : stats) {
            if (stat.getGameName().equalsIgnoreCase(gameName)) {
                return stat;
            }
        }
        return null;
    }
    public List<GameStat> getStats() { return stats; }
}

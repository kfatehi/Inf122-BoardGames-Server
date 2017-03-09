package team5.game;

/*
 * @brief   Data Structure that groups the game name and stats for a user.
 * @author  Team5
 * @date    2017/03/08
 */
public class GameStat {
    private String gameName;
    private int wins;
    private int losses;
    private int draws;

    public GameStat() {
        this.gameName = "";
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public GameStat(String gameName) {
        this.gameName = gameName;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public void incrementWins() { this.wins += 1; }
    public void incrementLosses() { this.losses += 1; }
    public void incrementDraws() { this.draws += 1; }

    public void setWins(int num) { this.wins = num; }
    public void setLosses(int num) { this.losses = num; }
    public void setDraws(int num) { this.draws = num; }
    public void setGameName(String gameName) { this.gameName = gameName; }

    public int getWins() { return this.wins; }
    public int getLosses() { return this.losses; }
    public int getDraws() { return this.draws; }
    public String getGameName() { return this.gameName; }

   
    
}

package hi.is.tournamentmanager.tm.model;

/**
 * Helper class for generating a scoreboard
 */
public class ScoreboardItem implements Comparable<ScoreboardItem> {

    private String team;
    private int gamesPlayed;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    public ScoreboardItem(){}
    public ScoreboardItem(String team, int gamesPlayed, int goalsFor, int goalsAgainst, int points) {
        this.team = team;
        this.gamesPlayed = gamesPlayed;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.points = points;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }
    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsFor() {
        return goalsFor;
    }
    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    @Override
    public int compareTo(ScoreboardItem o) {
        if (this.getPoints() > o.getPoints()) return 1;
        else if (this.getPoints() < o.getPoints()) return -1;
        else {
            if(this.getGoalsFor() - this.getGoalsAgainst() > o.getGoalsFor() - o.getGoalsAgainst()) return 1;
            else if(this.getGoalsFor() - this.getGoalsAgainst() < o.getGoalsFor() - o.getGoalsAgainst()) return -1;
            else {
                if(this.getGoalsAgainst() < o.getGoalsAgainst()) return 1;
                else if(this.getGoalsAgainst() > o.getGoalsAgainst()) return -1;
                else return this.getGoalsFor() - o.getGoalsFor();
            }
        }
    }
}


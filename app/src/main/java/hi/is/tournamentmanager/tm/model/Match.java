package hi.is.tournamentmanager.tm.model;

import java.io.Serializable;
import java.util.Date;

public class Match implements Comparable<Match>, Serializable {
    private long id;
    private int homeTeamScore;
    private int awayTeamScore;
    private Date matchDate;
    private int round;
    private String location;
    private String homeTeam;
    private String awayTeam;
    private boolean played;
    private long tournamentId;
    private long homeTeamId;
    private long awayTeamId;

    public Match() {
    }

    public Match(long id, String homeTeam, String awayTeam,long homeTeamId, long awayTeamId, int round, long tournamentId, boolean played) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.played = played;
        this.id = id;
        this.round = round;
        this.tournamentId = tournamentId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public long getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(long homeTeamId) { this.homeTeamId = homeTeamId; }

    public long getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(long awayTeamId) { this.awayTeamId = awayTeamId; }

    public long getTournament() { return tournamentId; }
    public void setTournament(long tournament) { this.tournamentId = tournament; }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }
    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }
    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public Date getMatchDate() {
        return matchDate;
    }
    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getRound() {
        return round;
    }
    public void setRound(int round) {
        this.round = round;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getPlayed() { return played; }
    public void setPlayed(boolean played) { this.played = played; }

    public int compareTo(Match o) {
        if (this.getRound() > o.getRound()) return 1;
        else if (this.getRound() < o.getRound()) return -1;
        else {
            if (this.getMatchDate() != null && o.getMatchDate() != null)
                return this.getMatchDate().compareTo(o.getMatchDate());
        }
        return 0;
    }
}
package hi.is.tournamentmanager.tm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tournament implements Serializable {

    private long id;
    private String name;
    private Date created;
    private Date signUpExpiration;
    private int maxTeams = 10;
    private int nrOfRounds = 2;
    private List<Team> teams = new ArrayList<>();
    private List<Match> matches = new ArrayList<>();
    private User user;
    private Sport sport = Sport.Football;
    private boolean isPublic = true;

    public Tournament() { }

    public Tournament(User user) { this.user = user; }

    public Tournament(String name, Date signUpExpiration, int maxTeams, int nrOfRounds, boolean isPublic) {
        this.name = name;
        this.signUpExpiration = signUpExpiration;
        this.maxTeams = maxTeams;
        this.nrOfRounds = nrOfRounds;
        this.isPublic = isPublic;
    }

    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getSignUpExpiration() {
        return signUpExpiration;
    }
    public void setSignUpExpiration(Date signUpExpiration) {
        this.signUpExpiration = signUpExpiration;
    }

    public int getMaxTeams() { return maxTeams; }
    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public boolean getIsPublic() { return isPublic; }
    public void setIsPublic(boolean isPublic) { this.isPublic = isPublic; }

    public int getNrOfRounds() { return nrOfRounds; }
    public void setNrOfRounds(int nrOfRounds) { this.nrOfRounds = nrOfRounds; }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }

    public User getUser() { return this.user; }
    public void setUser(User user) { this.user = user; }

    public Sport getSport() { return sport; }
    public void setSport(Sport sport) { this.sport = sport; }

    public void addTeam(Team team){
        teams.add(team);
    }
}


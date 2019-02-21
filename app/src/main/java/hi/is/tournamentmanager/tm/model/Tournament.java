package hi.is.tournamentmanager.tm.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tournament {

    private long id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime signUpExpiration;
    private int maxTeams = 10;
    private int nrOfRounds = 2;
    private Set<Team> teams = new HashSet<>();
    private List<Match> matches = new ArrayList<>();
    private User user;
    private Sport sport = Sport.Football;
    private boolean isPublic = true;

    public Tournament() { }

    public Tournament(User user) { this.user = user; }

    public Tournament(String name, LocalDateTime signUpExpiration, int maxTeams, int nrOfRounds, boolean isPublic) {
        this.name = name;
        this.signUpExpiration = signUpExpiration;
        this.maxTeams = maxTeams;
        this.nrOfRounds = nrOfRounds;
        this.isPublic = isPublic;
    }

    public Set<Team> getTeams() { return teams; }
    public void setTeams(Set<Team> teams) { this.teams = teams; }

    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getSignUpExpiration() {
        return signUpExpiration;
    }
    public void setSignUpExpiration(LocalDateTime signUpExpiration) {
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


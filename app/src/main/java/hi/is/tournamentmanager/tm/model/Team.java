package hi.is.tournamentmanager.tm.model;

public class Team {
    private long id;
    private String name;
    private Tournament tournament;

    public Team(){}
    public Team(String name, Tournament tournament){
        this.name = name;
        this.tournament = tournament;
    }

    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Tournament getTournament() { return this.tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }
}
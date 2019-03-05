package hi.is.tournamentmanager.tm.model;

import java.io.Serializable;

public class Team implements Serializable {
    private long id;
    private String name;
    private Tournament tournament;

    public Team(){}
    public Team(long id, String name){
        this.id = id;
        this.name = name;
    }
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
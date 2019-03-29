package hi.is.tournamentmanager.tm.helpers;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.Sport;
import hi.is.tournamentmanager.tm.model.Team;
import hi.is.tournamentmanager.tm.model.Tournament;

public class Mapper {

    public static List<Tournament> mapToTournamentList(JSONArray json){
        List<Tournament> tournaments = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject d = json.getJSONObject(i);
                Tournament t = mapToTournament(d);
                if (t != null) {
                    tournaments.add(t);
                }
            } catch (JSONException e) {
                Log.e("Mapper", e.toString());
            }
        }
        return tournaments;
    }

    public static List<Tournament> mapToSubscriptions(JSONArray json){
        List<Tournament> tournaments = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject d = json.getJSONObject(i);
                Tournament t = new Tournament();
                t = new Tournament();
                t.setName(d.getString("name"));
                t.setId(d.getLong("id"));
                t.setIsPublic(d.getBoolean("public"));
                t.setUser(d.getLong("userid"));
                Date signup = null;
                String dtStart = "2010-10-15T09:27:37Z";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                if(d.get("signupexpiration") != JSONObject.NULL)
                    signup = format.parse(d.getString("signupexpiration"));
                t.setSignUpExpiration(signup);
                tournaments.add(t);
            } catch (Exception e) {
                Log.e("Mapper", e.toString());
            }
        }
        return tournaments;
    }

    public static Tournament mapToTournament(JSONObject json){
        Tournament t;
        try{
            String name = json.getString("name");
            String dtStart = "2010-10-15T09:27:37Z";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date created = format.parse(json.getString("created"));
            Date signup = null;
            if(json.get("signupexpiration") != JSONObject.NULL)
                signup = format.parse(json.getString("signupexpiration"));
            Long owner = json.getLong("userid");
            int maxTeams = json.getInt("maxteams");
            int rounds = json.getInt("rounds");
            long id = json.getLong("id");
            boolean isPublic = json.getBoolean("public");
            List<Team> teams = mapToTeamsList(json.getJSONArray("teams"));
            List<Match> matches = mapToMatchList(json.getJSONArray("matches"), teams);
            Sport sport = Sport.values()[json.getInt("sport")];
            t = new Tournament();
            t.setName(name);
            t.setCreated(created);
            t.setMaxTeams(maxTeams);
            t.setNrOfRounds(rounds);
            t.setSignUpExpiration(signup);
            t.setId(id);
            t.setIsPublic(isPublic);
            t.setTeams(teams);
            t.setMatches(matches);
            t.setSport(sport);
            t.setUser(owner);
        } catch (Exception e) {
            t = null;
            Log.e("Mapper", e.toString());
        }
        return t;
    }

    private static List<Team> mapToTeamsList(JSONArray json){
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try{
                JSONObject o = json.getJSONObject(i);
                Team t = mapToTeam(o);
                if (t != null) {
                    teams.add(t);
                }
            } catch (JSONException e){
                Log.e("Mapper", e.toString());
            }
        }

        return teams;
    }

    private static Team mapToTeam(JSONObject json){
        Team team;
        try {
            String name = json.getString("name");
            long id = (long) (int) json.get("id");
            team = new Team(id, name);
        } catch (Exception e) {
            Log.e("Mapper", e.toString());
            team = null;
        }
        return team;
    }

    private static List<Match> mapToMatchList(JSONArray json, List<Team> teams){
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try{
                JSONObject o = json.getJSONObject(i);
                Match m = mapToMatch(o);
                if (m != null) {
                    matches.add(m);
                }
            } catch (Exception e){
                Log.e("Mapper", e.toString());
            }
        }

        return matches;
    }

    private static Match mapToMatch(JSONObject json){
        Match match;
        try {
            long id = (long) (int) json.get("id");
            String homeTeam = json.getString("hometeamname");
            String awayTeam = json.getString("awayteamname");
            long homeTeamId = json.getLong("hometeamid");
            long awayTeamId = json.getLong("awayteamid");
            int round = json.getInt("round");
            long tournamentid = json.getLong("tournamentid");
            boolean played = json.getBoolean("played");
            match = new Match(id, homeTeam, awayTeam, homeTeamId, awayTeamId, round, tournamentid, played);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date matchDate;
            if(json.get("matchdate") != JSONObject.NULL)
                match.setMatchDate(format.parse(json.getString("matchdate")));
            if(json.get("awayteamscore") != JSONObject.NULL)
                match.setAwayTeamScore(json.getInt("awayteamscore"));
            if(json.get("hometeamscore") != JSONObject.NULL)
                match.setHomeTeamScore(json.getInt("hometeamscore"));
            if(json.get("location") != JSONObject.NULL)
                match.setLocation(json.getString("location"));
        } catch (Exception e) {
            Log.e("Mapper", e.toString());
            match = null;
        }
        return match;
    }
}

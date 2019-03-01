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

    public static Tournament mapToTournament(JSONObject json){
        Tournament t;
        try{
            String name = json.getString("name");
            String dtStart = "2010-10-15T09:27:37Z";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = format.parse(json.getString("created"));
            Date signup;
            if(json.get("signUpExpiration") != JSONObject.NULL)
                signup = format.parse(json.getString("signUpExpiration"));
            String owner = json.getString("owner");
            int maxTeams = (int)json.get("maxTeams");
            int rounds = (int)json.get("nrOfRounds");
            long id = (long) (int) json.get("id");
            List<Team> teams = mapToTeamsList(json.getJSONArray("teams"));
            List<Match> matches = mapToMatchList(json.getJSONArray("matches"), teams);
            t = new Tournament();
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
            String homeTeam = json.getString("homeTeam");
            String awayTeam = json.getString("awayTeam");
            int homeTeamScore = (int)json.get("homeTeamScore");
            int awayTeamScore = (int)json.get("awayTeamScore");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date matchDate;
            if(json.get("signUpExpiration") != JSONObject.NULL)
                matchDate = format.parse(json.getString("matchDate"));
            match = new Match();
        } catch (Exception e) {
            Log.e("Mapper", e.toString());
            match = null;
        }
        return match;
    }
}

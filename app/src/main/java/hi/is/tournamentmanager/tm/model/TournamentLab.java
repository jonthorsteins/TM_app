package hi.is.tournamentmanager.tm.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;

public class TournamentLab {
    private static TournamentLab sTournamentLab;

    private List<Tournament> mTournaments;
    private List<Tournament> mMyTournaments;
    private List<Tournament> mSubscriptions;

    public static TournamentLab get(Context context) {
        System.out.println("Main constructor");
        if (sTournamentLab == null) sTournamentLab = new TournamentLab(context);
        return sTournamentLab;
    }

    public void setTournaments(List<Tournament> tournamentList) {
        mTournaments = tournamentList;
    }
    public void setMyTournaments(List<Tournament> tournamentList) {
        mMyTournaments = tournamentList;
    }
    public void setSubscriptions(List<Tournament> tournamentList) { mSubscriptions = tournamentList; }

    private TournamentLab(Context context) {
        System.out.println("Second constructor");
    }

    public List<Tournament> getTournaments() {
        return mTournaments;
    }
    public List<Tournament> getMyTournaments() {
        List<Tournament> list = new ArrayList<>();
        for(Tournament t : mMyTournaments)
            list.add(getTournament(t.getId()));
        return list;
    }
    public List<Tournament> getSubscriptions() {
        List<Tournament> list = new ArrayList<>();
        for(Tournament t : mSubscriptions)
            list.add(getTournament(t.getId()));
        return list;
    }

    public Tournament getTournament(long id) {
        for (Tournament t : mTournaments) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public void addTournament(Tournament tournament) {
        mTournaments.add(tournament);
    }
    public void addSubscription(Tournament tournament) {
        mSubscriptions.add(tournament);
    }
    public void addMyTournament(Tournament tournament) {
        mMyTournaments.add(tournament);
    }

    public void removeSubscription(Tournament tournament) {
        for (int i=0; i<mSubscriptions.size(); i++ ) {
            if (tournament.getId() == mSubscriptions.get(i).getId() ) {
                mSubscriptions.remove(i);
                return;
            }
        }
    }

    public void removeMyTournament(Tournament tournament) {
        for (int i=0; i<mMyTournaments.size(); i++ ) {
            if (tournament.getId() == mMyTournaments.get(i).getId() ) {
                mMyTournaments.remove(i);
                return;
            }
        }
    }

    public Tournament editTournament(Tournament tournament) {
        Tournament tt = new Tournament();
        for(Tournament t : mTournaments){
            if(t.getId() == tournament.getId()){
                t.setMaxTeams(tournament.getMaxTeams());
                t.setName(tournament.getName());
                t.setSignUpExpiration(tournament.getSignUpExpiration());
                t.setNrOfRounds(tournament.getNrOfRounds());
                t.setTeams(tournament.getTeams());
                t.setSport(tournament.getSport());
                tt = t;
            }
        }
        return tt;
    }

    public boolean isSubscribed(long id) {
        for(Tournament t: mSubscriptions){
            if(t.getId() == id) return true;
        }
        return false;
    }

    public boolean isOwner(long id, Tournament t){
        if(t.getUser() == id) return true;
        return false;
    }

    public void replaceTournament(Tournament t) {
        for (int i=0; i<mTournaments.size(); i++) {
            if ( mTournaments.get(i).getId() == t.getId() ) {
                mTournaments.set(i, t);
                break;
            }
        }
    }
}
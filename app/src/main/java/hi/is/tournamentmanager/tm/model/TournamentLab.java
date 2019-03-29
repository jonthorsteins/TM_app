package hi.is.tournamentmanager.tm.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public Tournament getTournament(UUID id) {
        for (Tournament t : mTournaments) {
            if (t.getUuid().equals(id)) {
                return t;
            }
        }

        System.out.println("Fjöldi tournamenta: " + mTournaments.size());
        return null;
    }

    public Tournament getTournament(Long id) {
        for (Tournament t : mTournaments) {
            if (t.getId() == id) {
                return t;
            }
        }

        System.out.println("Fjöldi tournamenta: " + mTournaments.size());
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

    public boolean isSubscribed(long id) {
        for(Tournament t: mSubscriptions){
            if(t.getId() == id) return true;
        }
        return false;
    }

    public boolean isOwner(long id){
        for(Tournament t: mSubscriptions){
            if(t.getUser() == id) return true;
        }
        return false;
    }
}
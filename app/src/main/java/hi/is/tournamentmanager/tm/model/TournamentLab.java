package hi.is.tournamentmanager.tm.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;

public class TournamentLab {
    private static TournamentLab sTournamentLab;

    private List<Tournament> mTournaments;

    public static TournamentLab get(Context context) {
        System.out.println("Main constructor");
        if (sTournamentLab == null) sTournamentLab = new TournamentLab(context);
        return sTournamentLab;
    }

    public void setTournaments(List<Tournament> tournamentList) {
        mTournaments = tournamentList;
    }

    private TournamentLab(Context context) {
        System.out.println("Second constructor");
    }

    public List<Tournament> getTournaments() {
        return mTournaments;
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
}
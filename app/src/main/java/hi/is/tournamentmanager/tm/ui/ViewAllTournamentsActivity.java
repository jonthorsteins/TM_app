package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.model.Tournament;

public class ViewAllTournamentsActivity extends AppCompatActivity {

    // private static

    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private boolean mAnswerIsTrue;
    TextView mViewAll;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, ViewAllTournamentsActivity.class);
        // i.putExtra(EXTRA_MESSAGE, answerIsTrue);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tournaments);
        mViewAll = (TextView) findViewById(R.id.viewAll);

        NetworkHandler.get("/tournaments",  null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try{
                    List<Tournament> tournaments = Mapper.mapToTournamentList(response.getJSONArray("tournaments"));
                } catch (JSONException e){
                    Log.e("Mapper", e.toString());
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response.toString());
                List<Tournament> tournaments = Mapper.mapToTournamentList(response);
            }
        });
    }
}

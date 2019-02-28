package hi.is.tournamentmanager.tm.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;

public class ViewAllTournamentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tournaments);

        NetworkHandler.get("/tournaments",  null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response.toString());

            }
        });
    }
}

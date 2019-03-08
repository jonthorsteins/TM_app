package hi.is.tournamentmanager.tm.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.model.Tournament;

public class ViewAllTournamentsActivity extends ListActivity {

    // private static
    private ListView mListView;

    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";
    private List<Tournament> mTournaments;
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
        final Context ct = getApplicationContext();


        NetworkHandler.get("/tournaments",  null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try{
                    mTournaments = Mapper.mapToTournamentList(response.getJSONArray("tournaments"));
                    TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, mTournaments);
                    setListAdapter(adapter);

                } catch (JSONException e){
                    Log.e("Mapper", e.toString());
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println(response.toString());
                mTournaments = Mapper.mapToTournamentList(response);
                TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, mTournaments);
                setListAdapter(adapter);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

            Intent intent = new Intent(this, ViewTournamentActivity.class);
            intent.putExtra(TOURNAMENT_ITEM, mTournaments.get(position));
            startActivity(intent);
    }

    public class TournamentArrayAdapter extends ArrayAdapter<Tournament>  {
        private final Context context;
        private final List<Tournament> values;

        public TournamentArrayAdapter(Context context, List<Tournament> values) {
            super(context, R.layout.tournament_list_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.tournament_list_row, parent, false);
            Tournament t = values.get(position);
            TextView textView = (TextView) rowView.findViewById(R.id.tournament_name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            textView.setText(t.getName());


            textView = (TextView) rowView.findViewById(R.id.tournament_status);
            Date currentDate = new Date();
            if (t.getMatches().size() != 0) {
                textView.setText("Started");
                textView.setTextColor(Color.parseColor("#000000"));
            } else if (t.getSignUpExpiration() == null ||
                       t.getSignUpExpiration().compareTo(currentDate) < 0) {
                textView.setText("Open");
                textView.setTextColor(Color.parseColor("#00FF00"));
            } else {
                textView.setText("Closed");
                textView.setTextColor(Color.parseColor("#FF00"));
            }
            // Change the icon for Windows and iPhone

            return rowView;
        }
    }
}

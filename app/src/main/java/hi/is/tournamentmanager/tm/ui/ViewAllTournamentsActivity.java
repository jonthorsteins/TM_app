package hi.is.tournamentmanager.tm.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;
import hi.is.tournamentmanager.tm.model.Sport;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;

public class ViewAllTournamentsActivity extends ListActivity {

    // private static
    private ListView mListView;
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";
    private List<Tournament> mTournaments;
    SharedPreferences mSharedPreferences;
    TextView mViewAll;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.profile:
                    i = new Intent(ViewAllTournamentsActivity.this, ViewProfileActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.view:
                    i = ViewAllTournamentsActivity.newIntent(ViewAllTournamentsActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.create:
                    i = new Intent(ViewAllTournamentsActivity.this, CreateTournamentActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
            }
            return false;
        }
    };


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, ViewAllTournamentsActivity.class);
        // i.putExtra(EXTRA_MESSAGE, answerIsTrue);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tournaments);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);

        final Context ct = getApplicationContext();

        TournamentLab.get(ct);
        String token = TokenStore.getToken(mSharedPreferences);

        NetworkHandler.get("/tournaments",  null, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try{
                    mTournaments = Mapper.mapToTournamentList(response.getJSONArray("tournaments"));

                    TournamentLab.get(getApplicationContext()).setTournaments(mTournaments);

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

                TournamentLab.get(getApplicationContext()).setTournaments(mTournaments);

                TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, mTournaments);
                setListAdapter(adapter);
            }
        });
        NetworkHandler.get("/profile", null, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try{
                    List<Tournament> mt = Mapper.mapToSubscriptions(response.getJSONArray("tournaments"));

                    TournamentLab.get(getApplicationContext()).setMyTournaments(mt);
                    List<Tournament> st = Mapper.mapToSubscriptions(response.getJSONArray("subscriptions"));
                    TournamentLab.get(getApplicationContext()).setSubscriptions(st);
                } catch (JSONException e){
                    Log.e("Mapper", e.toString());
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final EditText mSearch = findViewById(R.id.input_name);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Tournament> search = new ArrayList<Tournament>();
                if(mSearch.getText().toString().equals("")){
                    search = mTournaments;
                    final Context ct = getApplicationContext();
                    TournamentLab.get(getApplicationContext()).setTournaments(search);

                    TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, search);
                    setListAdapter(adapter);
                    return;
                }
                for(int i = 0; i < mTournaments.size(); i++){
                    if(mTournaments.get(i).getName().toLowerCase().indexOf(mSearch.getText().toString().toLowerCase()) == 0){
                        search.add(mTournaments.get(i));
                    }
                }
                final Context ct = getApplicationContext();
                TournamentLab.get(getApplicationContext()).setTournaments(search);

                TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, search);
                setListAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

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
            ImageView imageView = (ImageView) rowView.findViewById(R.id.sport_img);
            textView.setText(t.getName());

            if(t.getSport() == Sport.Basketball) {
                imageView.setImageResource(R.drawable.basketball);
            } else if(t.getSport() == Sport.Handball) {
                imageView.setImageResource(R.drawable.handball);
            }


            textView = (TextView) rowView.findViewById(R.id.tournament_status);
            Date currentDate = new Date();
            if (t.getMatches().size() != 0) {
                textView.setText("Started");
                textView.setTextColor(Color.parseColor("#000000"));
            } else if (t.getSignUpExpiration() == null ||
                       t.getSignUpExpiration().compareTo(currentDate) >= 0) {
                textView.setText("Open");
                textView.setTextColor(Color.parseColor("#00FF00"));
            } else {
                textView.setText("Closed");
                textView.setTextColor(Color.parseColor("#FF0000"));
            }
            // Change the icon for Windows and iPhone

            return rowView;
        }
    }
}

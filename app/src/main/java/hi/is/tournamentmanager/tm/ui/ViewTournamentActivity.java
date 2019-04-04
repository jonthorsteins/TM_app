package hi.is.tournamentmanager.tm.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;
import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.ScoreboardItem;
import hi.is.tournamentmanager.tm.model.Sport;
import hi.is.tournamentmanager.tm.model.Team;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;

public class ViewTournamentActivity extends AppCompatActivity implements MatchesFrag.RequestUpdate {
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String USER_PREFERENCE = "USER_PREFERENCE";

    private static final String TAG = "MainActivity";
    private Tournament t;
    private SharedPreferences mSharedPreferences;

    @Override
    public void requestUpdate(Tournament t) {
        // Get scoreboard fragment
        ScoreboardFrag frag = (ScoreboardFrag) getSupportFragmentManager().getFragments().get(0);
        frag.updateScoreboard(t);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.profile:
                    i = new Intent(ViewTournamentActivity.this, ViewProfileActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.view:
                    i = ViewAllTournamentsActivity.newIntent(ViewTournamentActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.create:
                    i = new Intent(ViewTournamentActivity.this, CreateTournamentActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tournament);
        t = (Tournament)getIntent().getSerializableExtra(TOURNAMENT_ITEM);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        List<ScoreboardItem> scoreboard = generateScoreboard(t);

        TextView textView  = findViewById(R.id.view_tournament_name);
        textView.setText(t.getName());
        if(TournamentLab.get(getApplicationContext()).isSubscribed(t.getId())) {
            findViewById(R.id.subscribe).setVisibility(View.GONE);
            findViewById(R.id.unsubscribe).setVisibility(View.VISIBLE);
            if(t.getSport() == Sport.Handball) {
                ((ImageView)findViewById(R.id.sport_img)).setImageResource(R.drawable.handball);
            } else if(t.getSport() == Sport.Basketball) {
                ((ImageView)findViewById(R.id.sport_img)).setImageResource(R.drawable.basketball);
            }
        }

        Long user = TokenStore.getUserId(mSharedPreferences);
        System.out.print("user id = " + user);
        System.out.print("user id t = " + t.getUser());
        if(t.getUser().equals(user)){
            findViewById(R.id.unsubscribe).setVisibility(View.GONE);
            findViewById(R.id.subscribe).setVisibility(View.GONE);
            findViewById(R.id.sport_img).setVisibility(View.GONE);
            findViewById(R.id.delete_tournament).setVisibility(View.VISIBLE);
            if(t.getMatches().size() == 0){
                findViewById(R.id.edit_tournament).setVisibility(View.VISIBLE);
                findViewById(R.id.start_tournament).setVisibility(View.VISIBLE);
            }
        }

        //startTournamentButtonSetup(t);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.d(TAG, "onCreate: Starting.");

        SectionsPageAdapter mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

 /*   private void startTournamentButtonSetup(Tournament t) {
        Long user = TokenStore.getUser(mSharedPreferences);
        if(TournamentLab.get(getApplicationContext()).isOwner(user) && t.getMatches().isEmpty()) {
                final Button mStartTourament = findViewById(R.id.btn_start_tournament);
                mStartTourament.setVisibility(View.VISIBLE);
                mStartTourament.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTourament();
                    }
                });
            final Button mEditTournament = findViewById(R.id.btn_edit_tournament);
            mEditTournament.setVisibility(View.VISIBLE);
            mEditTournament.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTournament();
                }
            });
        }
    }*/

    public void deleteTournament(View view){

        new AlertDialog.Builder(this)
                .setTitle("Delete tournament")
                .setMessage("Are you sure you want to delete this tournament?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        String token = TokenStore.getToken(mSharedPreferences);
                        Context context = getApplicationContext();
                        CharSequence text = "Delete tournament";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        NetworkHandler.delete("/tournaments/"+t.getId(), null, token, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                System.out.println(response.toString());
                                TournamentLab.get(getApplicationContext()).removeMyTournament(t);
                                Intent i = new Intent(ViewTournamentActivity.this, ViewAllTournamentsActivity.class);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                System.out.println(errorResponse.toString());
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.custom_toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView text = (TextView) layout.findViewById(R.id.text);
                                text.setText("Error deleting tournament");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.BOTTOM, 0, 50);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                        });
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void startTournament(View view) {
        System.out.println("Start Tournament");
        String token = TokenStore.getToken(mSharedPreferences);

        NetworkHandler.post("/tournaments/"+t.getId()+"/start", new JSONObject(), "application/json", token, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                Tournament mTournament = Mapper.mapToTournament(response);
                TournamentLab.get(getApplicationContext()).replaceTournament(mTournament);
                Intent i = new Intent(ViewTournamentActivity.this, ViewTournamentActivity.class);
                i.putExtra(TOURNAMENT_ITEM, Mapper.mapToTournament(response));
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse.toString());
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Error starting tournament");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public void editTournament(View view) {
        Intent i = new Intent(ViewTournamentActivity.this, EditTournamentActivity.class);
        i.putExtra(TOURNAMENT_ITEM, t);
        startActivity(i);
        finish();
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Tournament t = (Tournament)getIntent().getSerializableExtra(TOURNAMENT_ITEM);

        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ScoreboardFrag fragment1 = new ScoreboardFrag();
        fragment1 = fragment1.newInstance(t.getId());
        MatchesFrag fragment2 = new MatchesFrag();
        fragment2 = fragment2.newInstance(t.getId());
        adapter.addFragment(fragment1, "Scoreboard");
        adapter.addFragment(fragment2, "Matches");
        viewPager.setAdapter(adapter);

    }

    public List<ScoreboardItem> generateScoreboard(Tournament tournament) {
        int pointsForWin = 3; // 3 points for a win in football, 2 points otherwise
        if(tournament.getSport() != Sport.Football) pointsForWin = 2;

        List<Match> matches = tournament.getMatches();
        HashMap<Long, Integer> points = new HashMap<>();
        HashMap<Long, Integer> goalsFor = new HashMap<>();
        HashMap<Long, Integer> goalsAgainst = new HashMap<>();
        HashMap<Long, Integer> gamesPlayed = new HashMap<>();
        for(Match match: matches){
            if(match.getPlayed()){

                int home = match.getHomeTeamScore();
                int away = match.getAwayTeamScore();
                Long homeTeam = match.getHomeTeamId();
                Long awayTeam = match.getAwayTeamId();

                int playedHome = gamesPlayed.get(homeTeam) == null ? 0 : gamesPlayed.get(homeTeam);
                gamesPlayed.put(homeTeam, playedHome + 1);

                int playedAway = gamesPlayed.get(awayTeam) == null ? 0 : gamesPlayed.get(awayTeam);
                gamesPlayed.put(awayTeam, playedAway +1);

                int homeFor = goalsFor.get(homeTeam) == null ? 0 : goalsFor.get(homeTeam);
                goalsFor.put(homeTeam, homeFor + home);

                int awayFor = goalsFor.get(awayTeam) == null ? 0 : goalsFor.get(awayTeam);
                goalsFor.put(awayTeam, awayFor + away);

                int homeAgainst = goalsAgainst.get(homeTeam) == null ? 0 : goalsAgainst.get(homeTeam);
                goalsAgainst.put(homeTeam, homeAgainst + away);

                int awayAgainst = goalsAgainst.get(awayTeam) == null ? 0 : goalsAgainst.get(awayTeam);
                goalsAgainst.put(awayTeam, awayAgainst + home);

                if(home > away) {
                    int value = points.get(homeTeam) == null ? 0 : points.get(homeTeam);
                    points.put(homeTeam, value + pointsForWin);
                }
                else if(home < away) {
                    int value = points.get(awayTeam) == null ? 0 : points.get(awayTeam);
                    points.put(awayTeam, value + pointsForWin);
                }
                else {
                    int oldHome = points.get(homeTeam) == null ? 0 : points.get(homeTeam);
                    int oldAway = points.get(awayTeam) == null ? 0 : points.get(awayTeam);
                    points.put(homeTeam, oldHome + 1);
                    points.put(awayTeam, oldAway + 1);
                }
            }
        }


        return generateScoreboard(tournament.getTeams(), goalsFor,goalsAgainst,points, gamesPlayed);
    }



    private List<ScoreboardItem> generateScoreboard(List<Team> teams,
                                                    HashMap<Long,Integer> goalsFor,
                                                    HashMap<Long,Integer> goalsAgainst,
                                                    HashMap<Long,Integer> points,
                                                    HashMap<Long,Integer> gamesPlayed){
        List<ScoreboardItem> scoreboard= new ArrayList<>();
        for(Team team : teams){
            String name = team.getName();
            Long id = team.getId();
            scoreboard.add(new ScoreboardItem(
                    name,
                    gamesPlayed.get(id) == null ? 0 : gamesPlayed.get(id),
                    goalsFor.get(id) == null ? 0 : goalsFor.get(id),
                    goalsAgainst.get(id) == null ? 0 : goalsAgainst.get(id),
                    points.get(id) == null ? 0 : points.get(id))
            );
        }
        Collections.sort(scoreboard, Collections.reverseOrder());
        return scoreboard;
    }

    public void subscribe(View view){

        String token = TokenStore.getToken(mSharedPreferences);
        NetworkHandler.post("/tournaments/"+t.getId()+"/sub", new JSONObject(),"application/json", token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TournamentLab.get(getApplicationContext()).addSubscription(t);
                findViewById(R.id.subscribe).setVisibility(View.GONE);
                findViewById(R.id.unsubscribe).setVisibility(View.VISIBLE);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("You are now following this tournament");
                Toast toast = new Toast(getApplication());
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Error unsubscribing");
                Toast toast = new Toast(getApplication());
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public void unsubscribe(View view){
        String token = TokenStore.getToken(mSharedPreferences);
        NetworkHandler.delete("/tournaments/"+t.getId()+"/sub",null, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Context context = getApplicationContext();
                TournamentLab.get(context).removeSubscription(t);
                CharSequence text = "You have been unsubscribed!";
                int duration = Toast.LENGTH_SHORT;
                findViewById(R.id.subscribe).setVisibility(View.VISIBLE);
                findViewById(R.id.unsubscribe).setVisibility(View.GONE);

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Context context = getApplicationContext();
                CharSequence text = "Unsubscribe failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}

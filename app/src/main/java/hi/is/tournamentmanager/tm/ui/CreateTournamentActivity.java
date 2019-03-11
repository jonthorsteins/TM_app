package hi.is.tournamentmanager.tm.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;

public class CreateTournamentActivity extends AppCompatActivity {
    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.profile:
                    i = new Intent(CreateTournamentActivity.this, ViewProfileActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.view:
                    i = ViewAllTournamentsActivity.newIntent(CreateTournamentActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.create:
                    i = new Intent(CreateTournamentActivity.this, CreateTournamentActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
            }
            return false;
        }
    };

    SharedPreferences mSharedPreferences;


    EditText mName;
    EditText mMaxTeams;
    EditText mRounds;
    EditText mSignUp;
    EditText mTeamName;
    Button mAddTeam;
    Button mCreate;
    TextView mEmptyList;
    ListView mTeamsList;
    Spinner mSport;
    DatePicker mDatePicker;
    List<String> teams = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    String isoSignUp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mName = findViewById(R.id.input_name);
        mCreate = findViewById(R.id.btn_create_tournament);
        mMaxTeams = findViewById(R.id.input_maxTeams);
        mRounds = findViewById(R.id.input_rounds);
        mSignUp = findViewById(R.id.input_signUp);
        mDatePicker = findViewById(R.id.signUp_picker);
        mAddTeam = findViewById(R.id.btn_addTeam);
        mTeamName = findViewById(R.id.input_team_name);
        mTeamsList = findViewById(R.id.teams_list);
        mEmptyList = findViewById(R.id.teams_list_empty);
        mAdapter = new ArrayAdapter<String>(this, R.layout.addteam_list_row, R.id.item_team_name, teams);
        mTeamsList.setAdapter(mAdapter);
        mSport = findViewById(R.id.input_sport);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        mDatePicker.init(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), datePickerListener);

        mSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View focus = getCurrentFocus();
                if(focus != null){
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(
                            Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                    Objects.requireNonNull(getCurrentFocus()).clearFocus();
                }

                mDatePicker.setVisibility(View.VISIBLE);
            }
        });
        mAddTeam.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String input = mTeamName.getText().toString();
                int maxTeams = Integer.parseInt("0"+mMaxTeams.getText().toString());
                if(input.length() > 0 && teams.size() < maxTeams){
                    mTeamName.setText("");
                    teams.add(input);
                    mEmptyList.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mTeamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                System.out.println("Clicked: " + arg3);
                teams.remove((int)arg3);
                mAdapter.notifyDataSetChanged();
                if(teams.size() == 0) mEmptyList.setVisibility(View.VISIBLE);
            }
        });

        mCreate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // Mögulega að verifya input eitthvað
                createTournament();
            }
        });
    }

    private DatePicker.OnDateChangedListener datePickerListener = new DatePicker.OnDateChangedListener() {

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDatePicker.setVisibility(View.GONE);
            String year1 = String.valueOf(year);
            String month1 = String.valueOf(monthOfYear + 1);
            String day1 = String.valueOf(dayOfMonth);
            mSignUp.setText(day1 + "/" + month1 + "/" + year1);
            isoSignUp = year1 + "-" + month1 + "-" + day1;
        }
    };

    private void createTournament(){

        String name = mName.getText().toString();
        int maxTeams = Integer.parseInt("0"+mMaxTeams.getText().toString());
        maxTeams = maxTeams == 0 ? 2 : maxTeams;

        int rounds = Integer.parseInt("0"+ mRounds.getText().toString());
        rounds = rounds == 0 ? 2 : rounds;

        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("signUpExpiration", isoSignUp);
            body.put("teams", teams);
            body.put("maxTeams", maxTeams);
            body.put("rounds",rounds);
            body.put("sport", mSport.getSelectedItemPosition());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        String token = TokenStore.getToken(mSharedPreferences);
        NetworkHandler.post("/tournaments", body,"application/json", token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse.toString());
            }
        });

    }

}

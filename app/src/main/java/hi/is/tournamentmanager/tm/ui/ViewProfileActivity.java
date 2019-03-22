package hi.is.tournamentmanager.tm.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Date;
import java.util.List;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;
import hi.is.tournamentmanager.tm.model.User;


public class ViewProfileActivity extends ListActivity {

    // private static
    private ListView mListView;
    private User user = new User();

    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";
    private List<Tournament> mTournaments;
    private List<Tournament> mProfile;

    TextView mViewAll;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {
                case R.id.profile:
                    i = new Intent(ViewProfileActivity.this, ViewProfileActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.view:
                    i = ViewAllTournamentsActivity.newIntent(ViewProfileActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.create:
                    i = new Intent(ViewProfileActivity.this, CreateTournamentActivity.class);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
            }
            return false;
        }
    };


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, ViewProfileActivity.class);
        // i.putExtra(EXTRA_MESSAGE, answerIsTrue);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        final Context ct = getApplicationContext();

        TextView textElement = findViewById(R.id.username);
        mTournaments = TournamentLab.get(getApplicationContext()).getTournaments();
        user.setUsername("admin"); // þarf að breyta
        String username = (String) user.getUsername();
        textElement.setText(username);



        TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, mTournaments);
        setListAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                textView.setTextColor(Color.parseColor("#FF0000"));
            }

            return rowView;
        }
    }
}

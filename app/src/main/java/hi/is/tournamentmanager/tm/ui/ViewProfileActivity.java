package hi.is.tournamentmanager.tm.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.TokenStore;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;
import hi.is.tournamentmanager.tm.model.User;
import hi.is.tournamentmanager.tm.ui.fragments.MyTournamentsFragment;
import hi.is.tournamentmanager.tm.ui.fragments.SubscriptionsFragment;


public class ViewProfileActivity extends AppCompatActivity {

    // private static
    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    SharedPreferences mSharedPreferences;
    private List<Tournament> mTournaments;
    private List<Tournament> mProfile;
    ImageView mLogout;

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
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        mLogout = findViewById(R.id.logout);
        mTournaments = TournamentLab.get(getApplicationContext()).getTournaments();
        String username = TokenStore.getUserName(mSharedPreferences);
        ((TextView)findViewById(R.id.username_profile)).setText(username);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TokenStore.clearToken(mSharedPreferences);
                TokenStore.clearUser(mSharedPreferences);
                startActivity(new Intent(ViewProfileActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        Tournament t = (Tournament)getIntent().getSerializableExtra(TOURNAMENT_ITEM);

        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        MyTournamentsFragment fragment1 = new MyTournamentsFragment();
        fragment1 = fragment1.newInstance();
        SubscriptionsFragment fragment2 = new SubscriptionsFragment();
        fragment2 = fragment2.newInstance();
        adapter.addFragment(fragment1, "My tournaments");
        adapter.addFragment(fragment2, "My subscriptions");
        viewPager.setAdapter(adapter);

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

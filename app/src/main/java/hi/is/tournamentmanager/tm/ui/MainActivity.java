package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.TokenStore;


public class MainActivity extends AppCompatActivity {

    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    SharedPreferences mSharedPreferences;
    // private static final int REQUEST_CODE_VIEWALLTOURNAMENTS = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.profile:

                    return true;
                case R.id.view:
                    Intent i = ViewAllTournamentsActivity.newIntent(MainActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                    return true;
                case R.id.create:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        String token = TokenStore.getToken(mSharedPreferences);

        if (token.length() != 0) {
            Intent i = new Intent(this, ViewAllTournamentsActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

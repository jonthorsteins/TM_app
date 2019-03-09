package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.TokenStore;


public class MainActivity extends AppCompatActivity {

    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    SharedPreferences mSharedPreferences;
    // private static final int REQUEST_CODE_VIEWALLTOURNAMENTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        TokenStore.clearToken(mSharedPreferences); // Used to force login each time app is booted
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

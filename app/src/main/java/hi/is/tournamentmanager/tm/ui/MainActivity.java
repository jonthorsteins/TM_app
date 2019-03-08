package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;


public class MainActivity extends AppCompatActivity {

    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    SharedPreferences mSharedPreferences;
    private Button mSignInButton;
    private EditText mUsername;
    private EditText mPassword
            ;
    // private static final int REQUEST_CODE_VIEWALLTOURNAMENTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);

        String token = TokenStore.getToken(mSharedPreferences);

        if (token.length() != 0) {
            Intent i = ViewAllTournamentsActivity.newIntent(MainActivity.this, true);
            // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
            startActivity(i);
            finish();
        }

        mSignInButton = (Button) findViewById(R.id.signIn_button);
        mUsername = (EditText) findViewById(R.id.username_textField);
        mPassword = (EditText) findViewById(R.id.password_textField);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                System.out.println(username + ":" + password);
                // Set request body
                JSONObject body = new JSONObject();
                try {
                    body.put("username", username);
                    body.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                NetworkHandler.post("/login",  body, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            TokenStore.storeToken(mSharedPreferences, response.get("token").toString());
                            Intent intent = new Intent(MainActivity.this, ViewAllTournamentsActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Log.d("error", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        System.out.println(errorResponse.toString());
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(R.string.login_failed);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity( Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                });
                /*
                boolean verifyLogin = true; // kalla á eitthvað verification fall
                if (verifyLogin) {
                    Intent i = ViewAllTournamentsActivity.newIntent(MainActivity.this, true);
                    // startActivityForResult(i, REQUEST_CODE_VIEWALLTOURNAMENTS);
                    startActivity(i);
                    finish();
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText(R.string.login_failed);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity( Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                */
            }
        });

    }
}

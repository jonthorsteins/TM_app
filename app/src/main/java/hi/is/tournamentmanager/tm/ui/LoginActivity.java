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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;
import hi.is.tournamentmanager.tm.model.User;

public class LoginActivity extends AppCompatActivity {

    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";

    private ProgressBar mLoading;
    SharedPreferences mSharedPreferences;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);

        final Button mSignInButton = (Button) findViewById(R.id.signUp_button);
        final TextView mSignUpLink = (TextView) findViewById(R.id.signUp_link);
        mLoading = (ProgressBar)findViewById(R.id.signIn_spinner);
        mUsername = (EditText) findViewById(R.id.username_text);
        mPassword = (EditText) findViewById(R.id.password_text);

        mSignUpLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mSignInButton.setVisibility(View.GONE);
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
                NetworkHandler.post("/login", body, "application/json", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            TokenStore.storeToken(mSharedPreferences, response.get("token").toString());
                            String username = response.getJSONObject("user").getString("username");
                            Long userId = response.getJSONObject("user").getLong("id");
                            TokenStore.storeUser(mSharedPreferences, userId, username);
                            Intent intent = new Intent(LoginActivity.this, ViewAllTournamentsActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.d("error", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mLoading.setVisibility(View.GONE);
                        mSignInButton.setVisibility(View.VISIBLE);
                        mPassword.setText("");
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(R.string.login_failed);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                });
            }
        });

    }
}
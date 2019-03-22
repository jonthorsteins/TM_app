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

public class SignupActivity extends AppCompatActivity {

    public static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private ProgressBar mLoading;
    SharedPreferences mSharedPreferences;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPasswordAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSharedPreferences = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);
        final Button mSignUpButton = (Button) findViewById(R.id.signUp_button);
        mLoading = (ProgressBar)findViewById(R.id.signIn_spinner);
        mUsername = (EditText) findViewById(R.id.username_text);
        mPassword = (EditText) findViewById(R.id.password_text);
        mPasswordAgain = (EditText) findViewById(R.id.password_text_again);
        final TextView mSignInLink = (TextView) findViewById(R.id.signIn_link);

        mSignInLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mSignUpButton.setVisibility(View.GONE);
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String passwordAgain = mPasswordAgain.getText().toString();
                System.out.println(username + ":" + password + ":" + passwordAgain);

                // Athugar hvort lykilorð stemma
                if ( !password.equals(passwordAgain) ) {
                    mLoading.setVisibility(View.GONE);
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mPasswordAgain.setText("");
                    mPassword.setText("");
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText(R.string.signup_failed);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    return;
                }

                // Set request body
                JSONObject body = new JSONObject();
                try {
                    body.put("username", username);
                    body.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                NetworkHandler.post("/signup", body, "application/json", null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println(response.toString());
                        TokenStore.storeToken(mSharedPreferences,"");
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mLoading.setVisibility(View.GONE);
                        mSignUpButton.setVisibility(View.VISIBLE);
                        mPassword.setText("");
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(R.string.signup_failed);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        validate();
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        if (mUsername.length() <= 3  || mUsername == null) {
            mUsername.setError("at least 3 characters");
            valid = false;
        } else {
            mUsername.setError(null);
        }

        if ( !mPassword.equals(mPasswordAgain) ) {
            mPassword.setError("passwords do not match");
            mPasswordAgain.setError("passwords do not match");
            valid = false;
        } else {
            mPasswordAgain.setError(null);
            mPassword.setError(null);
        }

        if( mPassword.length() <=  6) {
            mPassword.setError("at least six characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }
}
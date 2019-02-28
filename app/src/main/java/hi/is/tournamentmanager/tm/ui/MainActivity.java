package hi.is.tournamentmanager.tm.ui;

import hi.is.tournamentmanager.tm.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private final String baseUrl = "http://localhost:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}

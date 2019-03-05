package hi.is.tournamentmanager.tm.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.model.Tournament;

public class ViewTournamentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tournament);
        Tournament t = (Tournament)getIntent().getSerializableExtra("TOURNAMENT_ITEM");
        System.out.println(t.getName());
    }


}

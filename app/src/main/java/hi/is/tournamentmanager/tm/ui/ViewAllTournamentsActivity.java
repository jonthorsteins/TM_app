package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hi.is.tournamentmanager.tm.R;

public class ViewAllTournamentsActivity extends AppCompatActivity {

    // private static

    private static final String EXTRA_MESSAGE = "is.hi.tournamentmanager.mainToAll";
    private boolean mAnswerIsTrue;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, ViewAllTournamentsActivity.class);
        // i.putExtra(EXTRA_MESSAGE, answerIsTrue);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tournaments);

        // mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_MESSAGE,false);
    }
}

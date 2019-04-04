package hi.is.tournamentmanager.tm.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.helpers.Mapper;
import hi.is.tournamentmanager.tm.helpers.NetworkHandler;
import hi.is.tournamentmanager.tm.helpers.TokenStore;
import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;


public class MatchesFrag extends Fragment {
    private static final String TAG = "MatchesFrag";

    private static final String TOURNAMENT_KEY = "tournament_key";
    private Tournament mTournament;
    private long tournamentId;
    public List<Match> mMatches;
    public MatchArrayAdapter mAdapter;
    private boolean isOwner;
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    SharedPreferences mSharedPreferences;
    RequestUpdate mUpdate;

    public interface RequestUpdate {
        public void requestUpdate(Tournament t);
    }

    public static MatchesFrag newInstance(long tournamentId) {

        MatchesFrag fragment = new MatchesFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TOURNAMENT_KEY, tournamentId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.matches_fragment,container,false);
        ListView listView = view.findViewById(R.id.list);
        mSharedPreferences = this.getActivity().getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE);


        Bundle args = getArguments();
        System.out.println(TOURNAMENT_KEY);
        tournamentId = (long) args.getSerializable(TOURNAMENT_KEY);
        mTournament = TournamentLab.get(getActivity()).getTournament(tournamentId);
        mMatches= mTournament.getMatches();

        long user = TokenStore.getUserId(mSharedPreferences);
        isOwner = TournamentLab.get(getActivity()).isOwner(user, mTournament);

        if (mMatches.isEmpty()) {
            view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            final Context ct = getActivity();
            mAdapter = new MatchArrayAdapter(ct, mMatches);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    final int editedMatchIndex = arg2;
                    Context context = getActivity();
                    final Match mClickedMatch = mMatches.get(arg2);
                    final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                    inputAlert.setTitle("Set score for Match");

                    final LayoutInflater inflater = getLayoutInflater();
                    final View view = inflater.inflate(R.layout.match_dialog, null);
                    inputAlert.setView(view);

                    final EditText mHomeTeamInputScore = view.findViewById(R.id.homeTeamInputScore);
                    final EditText mAwayTeamInputScore = view.findViewById(R.id.awayTeamInputScore);

                    final TextView mHomeTeamName = view.findViewById(R.id.alertHomeTeam);
                    mHomeTeamName.setText(mMatches.get(arg2).getHomeTeam());
                    final TextView mAwayTeamName = view.findViewById(R.id.alertAwayTeam);
                    mAwayTeamName.setText(mMatches.get(arg2).getAwayTeam());


                    inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mHomeTeamScore = mHomeTeamInputScore.getText().toString();
                            if (mHomeTeamScore.isEmpty()) {
                                mHomeTeamScore = "0";
                            } else {
                                for (int i=0; i < mHomeTeamScore.length(); i++) {
                                    char c = mHomeTeamScore.charAt(i);
                                    if (c < '0' || c > '9') {
                                        makeToast(view);
                                        return;
                                    }
                                }
                            }
                            String mAwayTeamScore = mAwayTeamInputScore.getText().toString();
                            if (mAwayTeamScore.isEmpty()) {
                                mAwayTeamScore = "0";
                            } else {
                                for (int i=0; i < mHomeTeamScore.length(); i++) {
                                    char c = mHomeTeamScore.charAt(i);
                                    if (c < '0' || c > '9') {
                                        makeToast(view);
                                        return;
                                    }
                                }
                            }

                            JSONObject body = new JSONObject();
                            try {
                                body.put("homeTeamScore", mHomeTeamScore);
                                body.put("awayTeamScore", mAwayTeamScore);
                                body.put("played", true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                            String token = TokenStore.getToken(mSharedPreferences);
                            NetworkHandler.patch("/tournaments/" + mClickedMatch.getTournament() +
                                    "/match/" + mClickedMatch.getId(), body,"application/json", token, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Tournament t = Mapper.mapToTournament(response);
                                    TournamentLab.get(getActivity()).replaceTournament(t);

                                    // Update the match list
                                    mMatches = t.getMatches();
                                    mAdapter.clear();
                                    mAdapter.addAll(mMatches);

                                    // Update the scoreboard
                                    mUpdate.requestUpdate(t);

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    System.out.println(errorResponse.toString());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.custom_toast,
                                            (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText(R.string.login_failed);
                                    Toast toast = new Toast(getContext());
                                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                }
                            });



                        }
                    });
                    inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = inputAlert.create();
                    alertDialog.show();
                }

            });
        }

        if (isOwner) {
            view.findViewById(R.id.matchHeader).findViewById(R.id.header_edit_match_filler).setVisibility(View.VISIBLE);
        }
        return view;
    }

    public class MatchArrayAdapter extends ArrayAdapter<Match> {
        private final Context context;
        private final List<Match> values;

        private MatchArrayAdapter(Context context, List<Match> values) {
            super(context, R.layout.match_list_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.match_list_row, parent, false);
            Match m = values.get(position);

            TextView textView = rowView.findViewById(R.id.home);
            textView.setText(m.getHomeTeam());


            // If game is played then score is displayed
            if (m.getPlayed()) {
                textView = rowView.findViewById(R.id.homeScore);
                textView.setText(Integer.toString(m.getHomeTeamScore()));

                textView = rowView.findViewById(R.id.awayScore);
                textView.setText(Integer.toString(m.getAwayTeamScore()));
            }

            textView = rowView.findViewById(R.id.away);
            textView.setText(m.getAwayTeam());

            textView = rowView.findViewById(R.id.round);
            textView.setText(Integer.toString(m.getRound()));

            if (isOwner) {
                rowView.findViewById(R.id.btn_editMatch).setVisibility(View.VISIBLE);
            }

            return rowView;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mUpdate = (RequestUpdate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    private void makeToast(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(R.string.toast_invalid_match_score);
        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
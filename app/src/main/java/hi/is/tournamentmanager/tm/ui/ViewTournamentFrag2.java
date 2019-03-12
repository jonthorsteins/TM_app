package hi.is.tournamentmanager.tm.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;


public class ViewTournamentFrag2 extends Fragment {
    private static final String TAG = "ViewTournamentFrag2";

    private static final String TOURNAMENT_KEY = "tournament_key";
    private Tournament mTournament;

    public static ViewTournamentFrag2 newInstance(UUID tournamentId) {

        ViewTournamentFrag2 fragment = new ViewTournamentFrag2();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TOURNAMENT_KEY, tournamentId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_view_tournament,container,false);
        ListView listView = view.findViewById(R.id.list);

        Bundle args = getArguments();
        System.out.println(TOURNAMENT_KEY);
        UUID tournamentId = (UUID) args.getSerializable(TOURNAMENT_KEY);
        mTournament = TournamentLab.get(getActivity()).getTournament(tournamentId);
        List<Match> mMatches= mTournament.getMatches();

        if (mMatches.isEmpty()) {
            view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            final Context ct = getActivity();
            MatchArrayAdapter adapter = new MatchArrayAdapter(ct, mMatches);
            listView.setAdapter(adapter);
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

            textView = rowView.findViewById(R.id.away);
            textView.setText(m.getAwayTeam());

            textView = rowView.findViewById(R.id.round);
            textView.setText(Integer.toString(m.getRound()));

            textView = rowView.findViewById(R.id.played);
            if (m.getPlayed()) {
                textView.setText("Played");
            } else {
                textView.setText("Not Played");
            }



            return rowView;
        }
    }
}
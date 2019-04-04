package hi.is.tournamentmanager.tm.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.ScoreboardItem;
import hi.is.tournamentmanager.tm.model.Sport;
import hi.is.tournamentmanager.tm.model.Team;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;
import hi.is.tournamentmanager.tm.ui.ViewAllTournamentsActivity;
import hi.is.tournamentmanager.tm.ui.ViewTournamentActivity;


public class SubscriptionsFragment extends Fragment {
    private static final String TAG = "SUBSCRIPTION_FRAGMENT";
    private static final String TOURNAMENT_ITEM = "TOURNAMENT_ITEM";

    public static SubscriptionsFragment newInstance() {
        return new SubscriptionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subscriptions,container,false);
        ListView listView = view.findViewById(R.id.list);

        final List<Tournament> mTournaments = TournamentLab.get(getActivity()).getSubscriptions();
        Context ct = getContext();
        TournamentArrayAdapter adapter = new TournamentArrayAdapter(ct, mTournaments);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Tournament temp = mTournaments.get(position);
                Tournament t = TournamentLab.get(getContext()).getTournament(temp.getId());
                Intent myIntent = new Intent(getActivity(), ViewTournamentActivity.class);
                myIntent.putExtra(TOURNAMENT_ITEM, t);
                startActivity(myIntent);
            }
        });

        return view;
    }

    public class TournamentArrayAdapter extends ArrayAdapter<Tournament>  {
        private final Context context;
        private final List<Tournament> values;

        public TournamentArrayAdapter(Context context, List<Tournament> values) {
            super(context, R.layout.tournament_list_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.tournament_list_row, parent, false);
            Tournament t = values.get(position);
            TextView textView = (TextView) rowView.findViewById(R.id.tournament_name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.sport_img);
            textView.setText(t.getName());

            if(t.getSport() == Sport.Basketball) {
                imageView.setImageResource(R.drawable.basketball);
            } else if(t.getSport() == Sport.Handball) {
                imageView.setImageResource(R.drawable.handball);
            }


            textView = (TextView) rowView.findViewById(R.id.tournament_status);
            Date currentDate = new Date();
            if (t.getMatches().size() != 0) {
                textView.setText("Started");
                textView.setTextColor(Color.parseColor("#000000"));
            } else if (t.getSignUpExpiration() == null ||
                    t.getSignUpExpiration().compareTo(currentDate) >= 0) {
                textView.setText("Open");
                textView.setTextColor(Color.parseColor("#00FF00"));
            } else {
                textView.setText("Closed");
                textView.setTextColor(Color.parseColor("#FF0000"));
            }

            return rowView;
        }
    }
}
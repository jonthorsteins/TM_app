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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import hi.is.tournamentmanager.tm.R;
import hi.is.tournamentmanager.tm.model.Match;
import hi.is.tournamentmanager.tm.model.ScoreboardItem;
import hi.is.tournamentmanager.tm.model.Sport;
import hi.is.tournamentmanager.tm.model.Team;
import hi.is.tournamentmanager.tm.model.Tournament;
import hi.is.tournamentmanager.tm.model.TournamentLab;


public class ViewTournamentFrag1 extends Fragment {
    private static final String TAG = "ViewTournamentFrag1";
    private static final String TOURNAMENT_KEY = "tournament_key";
    private Tournament mTournament;

    public static ViewTournamentFrag1 newInstance(UUID tournamentId) {

        ViewTournamentFrag1 fragment = new ViewTournamentFrag1();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TOURNAMENT_KEY, tournamentId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_view_tournament,container,false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        //container.addView(view.findViewById(R.id.scoreboardHeader));


        Bundle args = getArguments();
        System.out.println(TOURNAMENT_KEY);
        UUID tournamentId = (UUID) args.getSerializable(TOURNAMENT_KEY);
        mTournament = TournamentLab.get(getActivity()).getTournament(tournamentId);
        List<ScoreboardItem> scoreboard = generateScoreboard(mTournament);

        if (scoreboard.isEmpty()) {
            container.addView(view.findViewById(R.id.empty));
        } else {
            final Context ct = getActivity();
            ScoreboardArrayAdapter adapter = new ScoreboardArrayAdapter(ct, scoreboard);
            listView.setAdapter(adapter);
        }

        return view;
    }

    public class ScoreboardArrayAdapter extends ArrayAdapter<ScoreboardItem> {
        private final Context context;
        private final List<ScoreboardItem> values;

        public ScoreboardArrayAdapter(Context context, List<ScoreboardItem> values) {
            super(context, R.layout.scoreboard_list_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.scoreboard_list_row, parent, false);
            ScoreboardItem s = values.get(position);

            TextView textView = (TextView) rowView.findViewById(R.id.team);
            textView.setText(s.getTeam());

            textView = (TextView) rowView.findViewById(R.id.gamesPlayed);
            textView.setText(Integer.toString(s.getGamesPlayed()));

            textView = (TextView) rowView.findViewById(R.id.goalsFor);
            textView.setText(Integer.toString(s.getGoalsFor()));

            textView = (TextView) rowView.findViewById(R.id.goalsAgainst);
            textView.setText(Integer.toString(s.getGoalsAgainst()));

            textView = (TextView) rowView.findViewById(R.id.points);
            textView.setText(Integer.toString(s.getPoints()));



            return rowView;
        }
    }

    public List<ScoreboardItem> generateScoreboard(Tournament tournament) {
        int pointsForWin = 3; // 3 points for a win in football, 2 points otherwise
        if(tournament.getSport() != Sport.Football) pointsForWin = 2;

        List<Match> matches = tournament.getMatches();
        HashMap<Long, Integer> points = new HashMap<>();
        HashMap<Long, Integer> goalsFor = new HashMap<>();
        HashMap<Long, Integer> goalsAgainst = new HashMap<>();
        HashMap<Long, Integer> gamesPlayed = new HashMap<>();
        for(Match match: matches){
            if(match.getPlayed()){

                int home = match.getHomeTeamScore();
                int away = match.getAwayTeamScore();
                Long homeTeam = match.getHomeTeamId();
                Long awayTeam = match.getAwayTeamId();

                int playedHome = gamesPlayed.get(homeTeam) == null ? 0 : gamesPlayed.get(homeTeam);
                gamesPlayed.put(homeTeam, playedHome + 1);

                int playedAway = gamesPlayed.get(awayTeam) == null ? 0 : gamesPlayed.get(awayTeam);
                gamesPlayed.put(awayTeam, playedAway +1);

                int homeFor = goalsFor.get(homeTeam) == null ? 0 : goalsFor.get(homeTeam);
                goalsFor.put(homeTeam, homeFor + home);

                int awayFor = goalsFor.get(awayTeam) == null ? 0 : goalsFor.get(awayTeam);
                goalsFor.put(awayTeam, awayFor + away);

                int homeAgainst = goalsAgainst.get(homeTeam) == null ? 0 : goalsAgainst.get(homeTeam);
                goalsAgainst.put(homeTeam, homeAgainst + away);

                int awayAgainst = goalsAgainst.get(awayTeam) == null ? 0 : goalsAgainst.get(awayTeam);
                goalsAgainst.put(awayTeam, awayAgainst + home);

                if(home > away) {
                    int value = points.get(homeTeam) == null ? 0 : points.get(homeTeam);
                    points.put(homeTeam, value + pointsForWin);
                }
                else if(home < away) {
                    int value = points.get(awayTeam) == null ? 0 : points.get(awayTeam);
                    points.put(awayTeam, value + pointsForWin);
                }
                else {
                    int oldHome = points.get(homeTeam) == null ? 0 : points.get(homeTeam);
                    int oldAway = points.get(awayTeam) == null ? 0 : points.get(awayTeam);
                    points.put(homeTeam, oldHome + 1);
                    points.put(awayTeam, oldAway + 1);
                }
            }
        }


        return generateScoreboard(tournament.getTeams(), goalsFor,goalsAgainst,points, gamesPlayed);
    }



    private List<ScoreboardItem> generateScoreboard(List<Team> teams,
                                                    HashMap<Long,Integer> goalsFor,
                                                    HashMap<Long,Integer> goalsAgainst,
                                                    HashMap<Long,Integer> points,
                                                    HashMap<Long,Integer> gamesPlayed){
        List<ScoreboardItem> scoreboard= new ArrayList<>();
        for(Team team : teams){
            String name = team.getName();
            Long id = team.getId();
            scoreboard.add(new ScoreboardItem(
                    name,
                    gamesPlayed.get(id) == null ? 0 : gamesPlayed.get(id),
                    goalsFor.get(id) == null ? 0 : goalsFor.get(id),
                    goalsAgainst.get(id) == null ? 0 : goalsAgainst.get(id),
                    points.get(id) == null ? 0 : points.get(id))
            );
        }
        Collections.sort(scoreboard, Collections.reverseOrder());
        return scoreboard;
    }
}
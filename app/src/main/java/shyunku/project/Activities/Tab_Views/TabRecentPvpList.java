package shyunku.project.Activities.Tab_Views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.match.dto.ParticipantIdentity;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;
import java.util.List;

import shyunku.project.Engines.Adapters.PVPRecyclerAdapter;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Objects.PVPInfo;
import shyunku.project.R;

public class TabRecentPvpList extends Fragment {
    private RiotGameAPI api = new RiotGameAPI();

    private final ArrayList<PVPInfo> pvpList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PVPRecyclerAdapter adapter;

    private TextView progressBarRateView;
    private ProgressBar progressBarView;

    private ConstraintLayout originalLayout;
    private ConstraintLayout loadingLayout;

    private String playerID = null;
    private Summoner summoner = null;

    public TabRecentPvpList(String playerID) {
        this.playerID = playerID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_view_recent_pvp_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView)view.findViewById(R.id.pvpRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PVPRecyclerAdapter(pvpList);
        recyclerView.setAdapter(adapter);

        progressBarRateView = (TextView) view.findViewById(R.id.pvp_progressView);
        progressBarView = (ProgressBar) view.findViewById(R.id.pvp_progressBar);
        originalLayout = (ConstraintLayout)view.findViewById(R.id.originalLayout);
        loadingLayout = (ConstraintLayout)view.findViewById(R.id.loadingLayout);

        originalLayout.setVisibility(View.INVISIBLE);

        new getSummonerTask().execute(playerID);
    }

    private class getSummonerTask extends AsyncTask<String, Void, Summoner>{
        @Override
        protected Summoner doInBackground(String... strings) {
            String id = strings[0];
            Summoner summoner = new RiotGameAPI().getSummonerByID(id);
            return summoner;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            super.onPostExecute(summoner);
            setSummoner(summoner);

            new getChampionInfoTask().execute(summoner);
        }
    }

    private class getChampionInfoTask extends AsyncTask<Summoner, Double, ArrayList<ChampionMastery>> {

        @Override
        protected ArrayList doInBackground(Summoner... summoners) {
            Summoner summoner = summoners[0];
            MatchList matchList = new RiotGameAPI().getMatchListBySummoner(summoner);
            List<MatchReference> list = matchList.getMatches();

            final int cropSize = 25;
            for(int i=0;i<cropSize;i++){
                publishProgress((double)i/(double)cropSize);
                MatchReference ref = list.get(i);
                long gameID = ref.getGameId();
                Match match = new RiotGameAPI().getMatchByMatchID(gameID);
                int participantID = -1;
                if(match.getParticipantIdentities().size()==10) {
                    for (int j = 0; j < 10; j++) {
                        ParticipantIdentity playerIden = match.getParticipantIdentities().get(j);
                        if (playerIden.getPlayer().getAccountId().equals(summoner.getAccountId())) {
                            participantID = playerIden.getParticipantId();
                            break;
                        }
                    }
                }
                if(participantID == -1){
                    Log.e("ASSERT", "Error Code : 5");
                    return null;
                }
                //ParticipantStats
                ParticipantStats participantStats = match.getParticipants().get(participantID-1).getStats();
                pvpList.add(new PVPInfo(participantStats.isWin(), participantStats.getKills(),participantStats.getDeaths(),participantStats.getAssists(),
                        participantStats.getGoldEarned(), participantStats.getTotalMinionsKilled(), match.getGameCreation(), match.getGameDuration()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ChampionMastery> championMasteries) {
            super.onPostExecute(championMasteries);
            adapter.notifyDataSetChanged();

            loadingLayout.setVisibility(View.INVISIBLE);
            originalLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            double percent = values[0]*100;
            int percentage = (int)percent;
            progressBarRateView.setText(percentage+"%");
            progressBarView.setProgress(percentage);
        }
    }

    private void setSummoner(Summoner sum){
        this.summoner = sum;
    }
}

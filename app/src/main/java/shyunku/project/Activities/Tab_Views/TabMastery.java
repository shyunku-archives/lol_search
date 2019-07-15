package shyunku.project.Activities.Tab_Views;

import android.os.AsyncTask;
import android.os.Bundle;
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
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;

import shyunku.project.Engines.Adapters.MasteryRecyclerAdapter;
import shyunku.project.Engines.Logger;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Global.Statics;
import shyunku.project.Objects.CustomChampionInfo;
import shyunku.project.Objects.MasteryInfo;
import shyunku.project.R;

public class TabMastery extends Fragment {
    private RiotGameAPI api = new RiotGameAPI();

    private final ArrayList<MasteryInfo> masteryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MasteryRecyclerAdapter adapter;

    private String playerID = null;
    private Summoner summoner = null;

    private TextView progressBarRateView;
    private ProgressBar progressBarView;

    private ConstraintLayout loadingLayout;

    public TabMastery(String playerID) {
        this.playerID = playerID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_view_mastery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.masteryRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        progressBarRateView = (TextView) view.findViewById(R.id.mastery_progressView);
        progressBarView = (ProgressBar) view.findViewById(R.id.mastery_progressBar);

        loadingLayout = (ConstraintLayout)view.findViewById(R.id.masteryloadingLayout);

        adapter = new MasteryRecyclerAdapter(masteryList);
        recyclerView.setAdapter(adapter);

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

    private class getChampionInfoTask extends AsyncTask<Summoner, Double, ArrayList<ChampionMastery>>{

        @Override
        protected ArrayList doInBackground(Summoner... summoners) {
            Summoner summoner = summoners[0];
            ArrayList<ChampionMastery> mastery =api.getMasetryByID(summoner.getId());
            publishProgress(60D);
            int a = 0;
            Logger.Log("summonerID", summoner.getId());
            Logger.Log("puuID", summoner.getPuuid());
            Logger.Log("accountID", summoner.getAccountId());

            final int cropSize = 20;
            for(ChampionMastery cMastery : mastery){
                publishProgress((double)a/(double)cropSize+60);
                if(a>cropSize)break;
                CustomChampionInfo  info = Statics.getCustomChampionInfo(cMastery.getChampionId() + "");
                masteryList.add(new MasteryInfo(cMastery.isChestGranted(), cMastery.getChampionLevel(), cMastery.getChampionPoints(), info));
                a++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);

            double percent = values[0]*100;
            int percentage = (int)percent;

            progressBarRateView.setText(percentage+"%");
            progressBarView.setProgress(percentage);
        }

        @Override
        protected void onPostExecute(ArrayList<ChampionMastery> championMasteries) {
            super.onPostExecute(championMasteries);
            adapter.notifyDataSetChanged();

            loadingLayout.setVisibility(View.INVISIBLE);
        }
    }



    private void setSummoner(Summoner sum){
        this.summoner = sum;
    }
}

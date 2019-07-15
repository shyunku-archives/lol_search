package shyunku.project.Activities.Tab_Views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;

import shyunku.project.Engines.Logger;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Objects.MasteryInfo;
import shyunku.project.Objects.MasteryRecyclerAdapter;
import shyunku.project.R;

public class TabMastery extends Fragment {
    private RiotGameAPI api = new RiotGameAPI();
    private Summoner summoner = null;

    private final ArrayList<MasteryInfo> masteryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MasteryRecyclerAdapter adapter;

    private String playerID = null;

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

        adapter = new MasteryRecyclerAdapter(masteryList);
        recyclerView.setAdapter(adapter);

        new getChampionInfoTask().execute(playerID);
    }

    private class getChampionInfoTask extends AsyncTask<String, Void, ArrayList<ChampionMastery>>{

        @Override
        protected ArrayList doInBackground(String... strings) {
            Summoner summoner = new RiotGameAPI().getSummonerByID(strings[0]);
            ArrayList<ChampionMastery> mastery =api.getMasetryByID(summoner.getId());
            int a = 0;
            Logger.Log("summonerID", summoner.getId());
            Logger.Log("puuID", summoner.getPuuid());
            Logger.Log("accountID", summoner.getAccountId());
            for(ChampionMastery cMastery : mastery){
                if(a>5)break;
                Logger.Log("Requested", cMastery.getChampionId()+"");
                Champion c = api.getChampionByID(cMastery.getChampionId());
                masteryList.add(new MasteryInfo(cMastery.isChestGranted(), cMastery.getChampionLevel(), cMastery.getChampionPoints(), c));
                a++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ChampionMastery> championMasteries) {
            super.onPostExecute(championMasteries);
            adapter.notifyDataSetChanged();
        }
    }
}

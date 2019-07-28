package shyunku.project.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.rithms.riot.api.endpoints.league.dto.LeagueEntry;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import shyunku.project.Engines.Adapters.TabPagerAdapter;
import shyunku.project.Engines.ImageManager;
import shyunku.project.Engines.Logger;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Objects.SummonerInfo;
import shyunku.project.Objects.SummonerRank;
import shyunku.project.R;

public class InfoActivity extends AppCompatActivity {
    private SummonerInfo curPlayerInfo = null;
    Intent intent;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private RiotGameAPI localApi = new RiotGameAPI();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.summoner_info);
        intent = getIntent();
        String playerAccountID = intent.getStringExtra("PlayerID");
        if(playerAccountID == null)  toast("Error Code : 3");
        else new LoadSummonerTask().execute(playerAccountID);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("최근 전적"));
        tabLayout.addTab(tabLayout.newTab().setText("게임 모니터링"));
        tabLayout.addTab(tabLayout.newTab().setText("숙련도"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), playerAccountID);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int located = -1;
        for(int i=0;i<MainActivity.infos.size();i++) {
            if(curPlayerInfo == null)continue;
            if (MainActivity.infos.get(i).getSummoner().getAccountId().equals(curPlayerInfo.getSummoner().getAccountId())) {
                located = i;
                break;
            }
        }
        if(located == -1){
            toast("Error Code : 4");
            return;
        }
        curPlayerInfo.setMemo(((EditText)findViewById(R.id.currentPlayerMemo)).getText().toString());
        MainActivity.infos.set(located, curPlayerInfo);
        MainActivity.adapter.notifyDataSetChanged();
    }

    private class LoadSummonerTask extends AsyncTask<String, Void, Summoner>{
        @Override
        protected Summoner doInBackground(String... strings) {
            String id = strings[0];
            Summoner summoner = new RiotGameAPI().getSummonerByID(id);
            return summoner;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            super.onPostExecute(summoner);
            new getEntries().execute(summoner);
            new LoadSummonerBmp().execute(summoner);
            new LoadRankInfo().execute(summoner);
        }
    }

    private class LoadSummonerBmp extends AsyncTask<Summoner, Void, Bitmap>{
        Summoner received = null;
        SummonerRank srank = null;

        @Override
        protected Bitmap doInBackground(Summoner... summoners) {
            Summoner summoner = summoners[0];
            received = summoner;
            Bitmap bmp = new ImageManager().getBitmap("https://opgg-static.akamaized.net/images/profile_icons/profileIcon"+summoner.getProfileIconId()+".jpg");
            srank = new RiotGameAPI().getRepresentiveRank(received.getId());
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            String playerMemo = intent.getStringExtra("PlayerMemo");

            curPlayerInfo = new SummonerInfo(received, bitmap, playerMemo, srank);

            TextView nickname = (TextView)findViewById(R.id.currentPlayerNickname);
            TextView level = (TextView)findViewById(R.id.currentPlayerLevel);
            EditText memo = (EditText) findViewById(R.id.currentPlayerMemo);

            ImageView icon = (ImageView)findViewById(R.id.currentPlayerIcon);

            nickname.setText(curPlayerInfo.getSummoner().getName());
            level.setText(curPlayerInfo.getSummoner().getSummonerLevel()+" 레벨");
            memo.setText(curPlayerInfo.getMemo());
            icon.setImageBitmap(curPlayerInfo.getIconImage());
        }
    }

    private class getEntries extends  AsyncTask<Summoner, Void, Void>{
        RiotGameAPI lapi = new RiotGameAPI();
        @Override
        protected Void doInBackground(Summoner... summoners) {
            lapi.getLeagueEntrybyID(summoners[0].getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void toast(String msg){
        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(String msg){
        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private class LoadRankInfo extends AsyncTask<Summoner, Void, ArrayList<LeagueEntry>> {
        private ImageView soloRankIcon;
        private TextView soloTierView;
        private TextView soloTierPoint;

        private ImageView flexRankIcon;
        private TextView flexTierView;
        private TextView flexTierPoint;

        private ImageView TFTRankIcon;
        private TextView TFTTierView;
        private TextView TFTTierPoint;

        @Override
        protected ArrayList<LeagueEntry> doInBackground(Summoner... summoners) {
            Summoner summoner = summoners[0];
            RiotGameAPI rapi = new RiotGameAPI();
            String id = summoner.getId();

            LeagueEntry soloEntry = rapi.getSoloRankInfo(id);
            LeagueEntry flexEntry = rapi.getFlexRankInfo(id);
            LeagueEntry tftEntry = rapi.getTFTRankInfo(id);

            ArrayList<LeagueEntry> set = new ArrayList<>();

            set.add(soloEntry);
            set.add(flexEntry);
            set.add(tftEntry);

            return set;
        }

        @Override
        protected void onPostExecute(ArrayList<LeagueEntry> leagueEntry) {
            super.onPostExecute(leagueEntry);

            soloRankIcon = findViewById(R.id.SoloRankIcon);
            soloTierView = findViewById(R.id.SoloRankTier);
            soloTierPoint = findViewById(R.id.SoloRankPoint);

            flexRankIcon = findViewById(R.id.FlexRankIcon);
            flexTierView = findViewById(R.id.FlexRankTier);
            flexTierPoint = findViewById(R.id.FlexRankPoint);

            TFTRankIcon = findViewById(R.id.TFTRankIcon);
            TFTTierView = findViewById(R.id.TFTRankTier);
            TFTTierPoint = findViewById(R.id.TFTRankPoint);

            if(leagueEntry.get(0)!=null) {
                soloRankIcon.setImageBitmap(getEmblemBitmap(leagueEntry.get(0).getTier()));
                soloTierView.setText(formmater(leagueEntry.get(0).getTier()) + " " + leagueEntry.get(0).getRank());
                soloTierPoint.setText(leagueEntry.get(0).getLeaguePoints() + " LP");
            }

            if(leagueEntry.get(1)!=null) {
                flexRankIcon.setImageBitmap(getEmblemBitmap(leagueEntry.get(1).getTier()));
                flexTierView.setText(formmater(leagueEntry.get(1).getTier()) + " " + leagueEntry.get(1).getRank());
                flexTierPoint.setText(leagueEntry.get(1).getLeaguePoints() + " LP");
            }

            if(leagueEntry.get(2)!=null) {
                TFTRankIcon.setImageBitmap(getEmblemBitmap(leagueEntry.get(2).getTier()));
                TFTTierView.setText(formmater(leagueEntry.get(2).getTier()) + " " + leagueEntry.get(2).getRank());
                TFTTierPoint.setText(leagueEntry.get(2).getLeaguePoints() + " LP");
            }
        }

        private Bitmap getEmblemBitmap(String tier){
            String refined = "";
            for(int i=0;i<tier.length();i++)
                refined += (char)((int)tier.charAt(i) + (i>0?('a'-'A'):0));
            Logger.Log(""+refined);
            return getEmblemFile(refined);
        }

        public String formmater(String tier){
            if(tier.equals("IRON")) return "아이언";
            else if(tier.equals("BRONZE")) return "브론즈";
            else if(tier.equals("SILVER")) return "실버";
            else if(tier.equals("GOLD")) return "골드";
            else if(tier.equals("PLATINUM")) return "플레티넘";
            else if(tier.equals("DIAMOND")) return "다이아몬드";
            else if(tier.equals("MASTER")) return "마스터";
            else if(tier.equals("GRANDMASTER")) return "그랜드마스터";
            else if(tier.equals("CHALLENGER")) return "첼린저";
            return "ERROR";
        }

        private Bitmap getEmblemFile(String code){
            try {
                InputStream ims = null;
                ims = InfoActivity.this.getAssets().open("img/emblems/Emblem_"+code+".png");
                Logger.Log("TRY", "img/emblems/Emblem_"+code+".png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                return bmp;
            } catch (IOException e) {
                Logger.Log("FAIL");
                e.printStackTrace();
                return null;
            }
        }
    }
}

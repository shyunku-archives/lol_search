package shyunku.project.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
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

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import shyunku.project.Engines.Adapters.TabPagerAdapter;
import shyunku.project.Engines.ImageManager;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Objects.SummonerInfo;
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
        for(int i=0;i<MainActivity.infos.size();i++)
            if(MainActivity.infos.get(i).getSummoner().getAccountId() .equals(curPlayerInfo.getSummoner().getAccountId())){
                located = i;
                break;
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
            new LoadSummonerBmp().execute(summoner);
        }
    }

    private class LoadSummonerBmp extends AsyncTask<Summoner, Void, Bitmap>{
        Summoner received = null;

        @Override
        protected Bitmap doInBackground(Summoner... summoners) {
            Summoner summoner = summoners[0];
            received = summoner;
            Bitmap bmp = new ImageManager().getBitmap("https://opgg-static.akamaized.net/images/profile_icons/profileIcon"+summoner.getProfileIconId()+".jpg");
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            String playerMemo = intent.getStringExtra("PlayerMemo");

            curPlayerInfo = new SummonerInfo(received, bitmap, playerMemo);

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

    public void toast(String msg){
        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(String msg){
        Toast.makeText(InfoActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}

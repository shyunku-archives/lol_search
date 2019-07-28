package shyunku.project.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.util.ArrayList;

import shyunku.project.Engines.Adapters.PlayerRecyclerAdapter;
import shyunku.project.Engines.FileManager;
import shyunku.project.Engines.ImageManager;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Global.Statics;
import shyunku.project.Objects.SummonerInfo;
import shyunku.project.Objects.SummonerRank;
import shyunku.project.R;


public class MainActivity extends AppCompatActivity {
    RiotGameAPI api = new RiotGameAPI();
    Summoner summonerParameter;

    RecyclerView recyclerView;
    public static PlayerRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public static final ArrayList<SummonerInfo> infos = new ArrayList<>();

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            infos.remove(position);
            adapter.notifyItemRemoved(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OS VERSION", Build.VERSION.SDK_INT+"");
        super.onCreate(savedInstanceState);
        Statics.ChampionInfoJsonParser(getApplicationContext());
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.playerRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlayerRecyclerAdapter(infos);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        checkPermission();
        new FileManager().loadFile();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);
                final ConstraintLayout container = new ConstraintLayout(MainActivity.this);
                final ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
                params.rightMargin =getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
                editText.setLayoutParams(params);
                editText.setHint("플레이어 닉네임 입력");
                container.addView(editText);

                builder.setTitle("플레이어 추가");
                builder.setView(container);

                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    Summoner summoner = null;
                    Bitmap bitmap = null;
                    SummonerRank srank = null;
                    int status = 0;

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editText.getText().length()==0){
                            toast("최소 1자 이상 입력하세요!");
                            return;
                        }

                        final Handler handler = new Handler(){
                            public void handleMessage(Message msg){
                                if(status==1){
                                    toast("Error Code : 1");
                                    return;
                                }

                               final Handler handler2 = new Handler(){
                                   public void handleMessage(Message msg){
                                       if(status == 2){
                                           toast("Error Code : 2");
                                           return;
                                       }
                                       infos.add(new SummonerInfo(summoner, bitmap, "메모 없음", srank));
                                       adapter.notifyDataSetChanged();
                                   }
                               };
                                new Thread(){
                                    public void run(){
                                        ImageManager manager = new ImageManager();
                                        Bitmap bmp = manager.getBitmap("https://opgg-static.akamaized.net/images/profile_icons/profileIcon"+summoner.getProfileIconId()+".jpg");
                                        setRank(new RiotGameAPI().getRepresentiveRank(summoner.getId()));
                                        setBitmap(bmp);
                                        handler2.sendMessage(handler2.obtainMessage());
                                        if(bmp == null){
                                            setStatus(2);
                                            return;
                                        }
                                    }
                                }.start();
                            }
                        };

                        new Thread(){
                            public void run(){
                                Summoner summoner = api.getSummonerByNickname(editText.getText().toString());
                                setSummoner(summoner);
                                handler.sendMessage(handler.obtainMessage());
                                if(summoner == null){
                                    setStatus(1);
                                    return;
                                }
                            }
                        }.start();
                    }

                    public void setSummoner(Summoner s){
                        this.summoner = s;
                    }
                    public void setBitmap(Bitmap b){
                        bitmap = b;
                    }
                    public void setStatus(int i){status = i;}
                    public void setRank(SummonerRank rank){this.srank = rank;}
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar player_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toast(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new FileManager().saveFile();
    }

    public void checkPermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}

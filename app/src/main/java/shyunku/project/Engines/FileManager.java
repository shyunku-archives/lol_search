package shyunku.project.Engines;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import shyunku.project.Activities.MainActivity;
import shyunku.project.Global.RiotGameAPI;
import shyunku.project.Objects.SummonerInfo;

public class FileManager {
    public void saveFile(){
        Log.e("try", "saving");

        ArrayList<SummonerInfo> infos = MainActivity.infos;
        File saveFile = null;
        saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lolSaveFiles");

        if(!saveFile.exists())
            saveFile.mkdir();
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/savedSummonerIDList.txt", false));
            for(int i=0;i<infos.size();i++){
                buf.append(infos.get(i).getSummoner().getAccountId()+"|"+infos.get(i).getMemo());
                buf.newLine();
            }
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(){
        Log.e("try", "loading");
        String readLine = null;
        File saveFile = null;

        saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lolSaveFiles");
        if(saveFile == null)
            saveFile.mkdir();
        try {
            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/savedSummonerIDList.txt"));
            MainActivity.infos.clear();
            while ((readLine = buf.readLine()) != null) {
                String parsed[] = readLine.split("\\|");
                new LoadSummonerTask().execute(parsed[0], parsed[1]);
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LoadSummonerTask extends AsyncTask<String, Void, Summoner>{
        String memo = null;
        @Override
        protected Summoner doInBackground(String... strings) {
            String id = strings[0];
            memo = strings[1];
            Summoner summoner = new RiotGameAPI().getSummonerByID(id);
            return summoner;
        }

        @Override
        protected void onPostExecute(Summoner summoner) {
            super.onPostExecute(summoner);
            LoadSummonerBmp getBmp = new LoadSummonerBmp();
            getBmp.setMemo(memo);
            getBmp.execute(summoner);
        }
    }

    private class LoadSummonerBmp extends AsyncTask<Summoner, Void, Bitmap> {
        Summoner received = null;
        String memo = null;

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
            MainActivity.infos.add(new SummonerInfo(received, bitmap, memo));
            MainActivity.adapter.notifyDataSetChanged();
        }

        public void setMemo(String memo){
            this.memo = memo;
        }
    }

    public class saveInfo{
        public String ID;
        public String memo;

        public saveInfo(String ID, String memo) {
            this.ID = ID;
            this.memo = memo;
        }
    }
}

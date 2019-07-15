package shyunku.project.Global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import shyunku.project.Engines.Logger;
import shyunku.project.Objects.CustomChampionInfo;

public class Statics {
    public static ArrayList<CustomChampionInfo> CustomChampionList = new ArrayList<>();

    public static CustomChampionInfo getCustomChampionInfo(String ID){
        for(CustomChampionInfo info : CustomChampionList) {
            if (info.getChampionKey().equals(ID))
                return info;
        }
        return null;
    }

    public static void ChampionInfoJsonParser(Context context){
        String json = loadChampionInfoJSONFromAsset(context);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject Data = jsonObject.getJSONObject("data");

            Iterator<String> keys = Data.keys();
            while(keys.hasNext()){
                String key = keys.next();
                JSONObject champObject = Data.getJSONObject(key);
                String ChampionKey = champObject.optString("key");
                String ChampionName = champObject.optString("name");
                String ChampionID = champObject.optString("id");

                Image icon = null;
                InputStream ims = context.getAssets().open("img/"+ChampionID+".png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);

                Statics.CustomChampionList.add(new CustomChampionInfo(ChampionKey, ChampionName, ChampionID, bmp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.Log("ChampionInfo", "Download Complete");
    }

    private static String loadChampionInfoJSONFromAsset(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("champion.json");
            int size = is.available();
            byte[] buff = new byte[size];
            is.read(buff);
            is.close();
            json = new String(buff, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}

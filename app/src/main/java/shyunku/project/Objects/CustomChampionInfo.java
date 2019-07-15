package shyunku.project.Objects;

import android.graphics.Bitmap;

public class CustomChampionInfo {
    private String ChampionKey;
    private String ChampionName;
    private String ChampionID;
    private Bitmap image;

    public String getChampionKey() {
        return ChampionKey;
    }

    public CustomChampionInfo(String championKey, String championName, String id, Bitmap image) {
        ChampionKey = championKey;
        ChampionName = championName;
        this.ChampionID = id;
        this.image = image;
    }

    public void setChampionKey(String championKey) {
        ChampionKey = championKey;
    }

    public String getChampionName() {
        return ChampionName;
    }

    public void setChampionName(String championName) {
        ChampionName = championName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getChampionID() {
        return ChampionID;
    }

    public void setChampionID(String championID) {
        ChampionID = championID;
    }
}

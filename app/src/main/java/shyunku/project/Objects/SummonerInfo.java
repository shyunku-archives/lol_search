package shyunku.project.Objects;

import android.graphics.Bitmap;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

public class SummonerInfo {
    private Summoner summoner;
    private Bitmap iconImage;
    private String memo;

    public SummonerInfo(Summoner s, Bitmap i, String memo){
        summoner = s;
        iconImage = i;
        this.memo = memo;
    }

    public Summoner getSummoner() {
        return summoner;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    public Bitmap getIconImage() {
        return iconImage;
    }

    public void setIconImage(Bitmap iconImage) {
        this.iconImage = iconImage;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

package shyunku.project.Objects;

import android.graphics.Bitmap;

import net.rithms.riot.api.endpoints.summoner.dto.Summoner;

public class SummonerInfo {
    private Summoner summoner;
    private Bitmap iconImage;
    private String memo;
    private SummonerRank srank;

    public SummonerInfo(Summoner s, Bitmap i, String memo, SummonerRank rank){
        summoner = s;
        iconImage = i;
        this.memo = memo;
        this.srank = rank;
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

    public SummonerRank getRepresentedRank() {
        return srank;
    }

    public void setRepresentedRank(SummonerRank representedRank) {
        this.srank = representedRank;
    }
}

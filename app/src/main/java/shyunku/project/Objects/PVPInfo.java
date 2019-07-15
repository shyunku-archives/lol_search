package shyunku.project.Objects;

import android.graphics.Bitmap;

public class PVPInfo {
    private boolean isWIn;
    private int killCount;
    private int dieCount;
    private int assistCount;
    private long gainedGold;
    private long csCount;
    private long startFlag;
    private long duration;
    private Bitmap bitmap;

    public PVPInfo(boolean isWIn, int killCount, int dieCount, int assistCount, long gainedGold, long csCount, long startFlag, long duration, Bitmap bmp) {
        this.isWIn = isWIn;
        this.killCount = killCount;
        this.dieCount = dieCount;
        this.assistCount = assistCount;
        this.gainedGold = gainedGold;
        this.csCount = csCount;
        this.startFlag = startFlag;
        this.duration = duration;
        this.bitmap = bmp;
    }

    public boolean isWIn() {
        return isWIn;
    }

    public void setWIn(boolean WIn) {
        isWIn = WIn;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public int getDieCount() {
        return dieCount;
    }

    public void setDieCount(int dieCount) {
        this.dieCount = dieCount;
    }

    public int getAssistCount() {
        return assistCount;
    }

    public void setAssistCount(int assistCount) {
        this.assistCount = assistCount;
    }

    public long getGainedGold() {
        return gainedGold;
    }

    public void setGainedGold(long gainedGold) {
        this.gainedGold = gainedGold;
    }

    public long getCsCount() {
        return csCount;
    }

    public void setCsCount(long csCount) {
        this.csCount = csCount;
    }

    public long getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(long startFlag) {
        this.startFlag = startFlag;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

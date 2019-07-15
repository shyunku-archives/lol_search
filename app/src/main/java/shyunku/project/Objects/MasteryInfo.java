package shyunku.project.Objects;

public class MasteryInfo {
    private boolean chestGranted;
    private int championLevel;
    private long championPoint;
    private CustomChampionInfo champInfo;

    public MasteryInfo(boolean chestGranted, int championLevel, long championPoint, CustomChampionInfo champion) {
        this.chestGranted = chestGranted;
        this.championLevel = championLevel;
        this.championPoint = championPoint;
        this.champInfo = champion;
    }

    public boolean isChestGranted() {
        return chestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.chestGranted = chestGranted;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    public long getChampionPoint() {
        return championPoint;
    }

    public void setChampionPoint(long championPoint) {
        this.championPoint = championPoint;
    }

    public CustomChampionInfo getChampInfo() {
        return champInfo;
    }

    public void setChampInfo(CustomChampionInfo champInfo) {
        this.champInfo = champInfo;
    }
}

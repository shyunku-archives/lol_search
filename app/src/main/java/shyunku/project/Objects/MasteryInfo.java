package shyunku.project.Objects;

import net.rithms.riot.api.endpoints.static_data.dto.Champion;

public class MasteryInfo {
    private boolean chestGranted;
    private int championLevel;
    private long championPoint;
    private Champion champion;

    public MasteryInfo(boolean chestGranted, int championLevel, long championPoint, Champion champion) {
        this.chestGranted = chestGranted;
        this.championLevel = championLevel;
        this.championPoint = championPoint;
        this.champion = champion;
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

    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }
}

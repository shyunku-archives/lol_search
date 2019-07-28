package shyunku.project.Objects;

public class SummonerRank {
    private String tier;
    private String rank;
    private int point;
    private String queueType;

    public SummonerRank(String tier, String rank, int point, String queueType) {
        this.tier = tier;
        this.rank = rank;
        this.point = point;
        this.queueType = queueType;
    }

    public String getRefinedTier(){
        if(tier.equals(""))return "";
        String res = "";
        if(tier.equals("IRON")) res += "아이언";
        else if(tier.equals("BRONZE")) res += "브론즈";
        else if(tier.equals("SILVER")) res += "실버";
        else if(tier.equals("GOLD")) res += "골드";
        else if(tier.equals("PLATINUM")) res += "플레티넘";
        else if(tier.equals("DIAMOND")) res += "다이아몬드";
        else if(tier.equals("MASTER")) res += "마스터";
        else if(tier.equals("GRANDMASTER")) res += "그랜드마스터";
        else if(tier.equals("CHALLENGER")) res += "첼린저";
        else res += "E";
        res += " "+rank;

        return res;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }
}

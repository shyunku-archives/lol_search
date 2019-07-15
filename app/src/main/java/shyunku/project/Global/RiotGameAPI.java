package shyunku.project.Global;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchFrame;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionList;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RiotGameAPI {
    public static final String API_KEY = "RGAPI-e4175db4-5bb4-43f1-bb87-ffdd8258c091";
    public static final Platform platform = Platform.KR;
    private ApiConfig config = new ApiConfig().setKey(API_KEY);
    RiotApi api = new RiotApi(config);

    public Summoner getSummonerByNickname(String nickname){
        Summoner summoner = null;
        try {
            summoner = api.getSummonerByName(platform, nickname);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return summoner;
    }

    public Summoner getSummonerByID(String ID){
        Summoner summoner = null;
        try {
            summoner = api.getSummonerByAccount(platform, ID);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return summoner;
    }

    public ArrayList<ChampionMastery> getMasetryByID(String ID){
        ArrayList<ChampionMastery> mastery = null;
        try {
           mastery = new ArrayList<ChampionMastery>(api.getChampionMasteriesBySummoner(platform, ID));
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return mastery;
    }

    public Map<String, Champion> getChampionInfo(){
        ChampionList championList = null;
        Map<String, Champion> map = null;
        try {
            championList = api.getDataChampionList(platform);
            map = championList.getData();
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Champion getChampionByID(int id){
        Champion champion = null;
        try {
            champion = api.getDataChampion(platform, id);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return champion;
    }

    public MatchList getMatchListBySummoner(Summoner summoner){
        MatchList list = null;
        try {
            list =  api.getMatchListByAccountId(platform, summoner.getAccountId());
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Match getMatchByMatchID(long id){
        Match match = null;
        try {
            match = api.getMatch(platform, id);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return match;
    }

    public MatchFrame getMatchParticipantFramebyID(long matchID){
        MatchTimeline timeline = null;
        MatchFrame lastFrame = null;

        try {
            timeline = api.getTimelineByMatchId(platform, matchID);
            List<MatchFrame> frameList = timeline.getFrames();
            lastFrame = frameList.get(frameList.size()-1);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }

        return lastFrame;
    }
}

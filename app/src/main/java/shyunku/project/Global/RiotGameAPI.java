package shyunku.project.Global;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.league.dto.LeagueEntry;
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
import java.util.Set;

import shyunku.project.Engines.Logger;
import shyunku.project.Objects.SummonerRank;

public class RiotGameAPI {
    public static final String API_KEY = "RGAPI-f39dc642-e5b8-48da-9585-4577556a2f1e";
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

    public Set<LeagueEntry> getLeagueEntrybyID(String id){
        Set<LeagueEntry> entries = null;
        try {
            entries =  api.getLeagueEntriesBySummonerId(platform, id);
            for(LeagueEntry entry : entries){
                Logger.Log("ENTRY", entry.getTier()+" "+entry.getRank()+" "+entry.getLeaguePoints()+" "+entry.getQueueType());
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public SummonerRank getRepresentiveRank(String id){
        Set<LeagueEntry> entries = null;
        try {
            entries = api.getLeagueEntriesBySummonerId(platform, id);
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_SOLO_5x5"))
                    return new SummonerRank(entry.getTier(), entry.getRank(), entry.getLeaguePoints(), "개인/2인전");
            }
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_FLEX_SR"))
                    return new SummonerRank(entry.getTier(), entry.getRank(), entry.getLeaguePoints(), "자유 5대5 대전");
            }
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_TFT"))
                    return new SummonerRank(entry.getTier(), entry.getRank(), entry.getLeaguePoints(), "전략적 팀 전투");
            }
            if(entries.size()>0){
                //기타 모드가 대표 모드
                for(LeagueEntry entry : entries)
                    return new SummonerRank(entry.getTier(), entry.getRank(), entry.getLeaguePoints(), entry.getQueueType());
            }
            return new SummonerRank("", "", 0, "랭크 없음");
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LeagueEntry getSoloRankInfo(String id){
        try {
            Set<LeagueEntry> entries = api.getLeagueEntriesBySummonerId(platform, id);
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_SOLO_5x5"))
                    return entry;
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LeagueEntry getFlexRankInfo(String id){
        try {
            Set<LeagueEntry> entries = api.getLeagueEntriesBySummonerId(platform, id);
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_FLEX_SR"))
                    return entry;
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LeagueEntry getTFTRankInfo(String id){
        try {
            Set<LeagueEntry> entries = api.getLeagueEntriesBySummonerId(platform, id);
            for(LeagueEntry entry : entries) {
                if (entry.getQueueType().equals("RANKED_TFT"))
                    return entry;
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package shyunku.project.Global;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class RiotGameAPI {
    public static final String API_KEY = "RGAPI-2db54454-10e9-4f2c-b240-64a7ec792410";
    public static final Platform platform = Platform.KR;

    public Summoner getSummonerByNickname(String nickname){
        ApiConfig config = new ApiConfig().setKey(API_KEY);
        RiotApi api = new RiotApi(config);

        Summoner summoner = null;
        try {
            summoner = api.getSummonerByName(platform, nickname);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return summoner;
    }

    public Summoner getSummonerByID(String ID){
        ApiConfig config = new ApiConfig().setKey(API_KEY);
        RiotApi api = new RiotApi(config);

        Summoner summoner = null;
        try {
            summoner = api.getSummonerByAccount(platform, ID);
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return summoner;
    }
}

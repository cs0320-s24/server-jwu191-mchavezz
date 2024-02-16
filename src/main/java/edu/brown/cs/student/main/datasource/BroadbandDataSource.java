package edu.brown.cs.student.main.datasource;

import edu.brown.cs.student.main.server.BroadbandUtilities;
import kotlin.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadbandDataSource {

    private final Map<String, String> stateIDMap;
    private final Map<String, List<String>> countyIDMap;

    public BroadbandDataSource() throws URISyntaxException, IOException, InterruptedException {
        this.stateIDMap = new HashMap<>();
        this.countyIDMap = new HashMap<>();
        this.populateIDMaps();
    }

    private void populateIDMaps() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest buildACSApiRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
                        .GET()
                        .build();

        HttpResponse<String> sentACSApiResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildACSApiRequest, HttpResponse.BodyHandlers.ofString());

        List<List<String>> stateList = BroadbandUtilities.deserializeResponse(sentACSApiResponse.body());
        for(List<String> state : stateList) {
            this.stateIDMap.put(state.get(0), state.get(1));
        }
        buildACSApiRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*"))
                        .GET()
                        .build();

        sentACSApiResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildACSApiRequest, HttpResponse.BodyHandlers.ofString());

        List<List<String>> countyList = BroadbandUtilities.deserializeResponse(sentACSApiResponse.body());
        for(List<String> county : countyList) {
            // Could use subList here, but this is probably safer
            this.countyIDMap.put(county.get(0).split(",")[0], Arrays.asList(county.get(2), county.get(1)));
        }
    }

    private Pair<String, String> retrieveIDs(String state, String county) {
        return new Pair<>(this.stateIDMap.get(state) , this.countyIDMap.get(county).get(0));
    }
    public String sendRequest(String state, String county, String key) throws URISyntaxException, IOException, InterruptedException {
        // example: https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06
        Pair<String, String> ids = this.retrieveIDs(state, county);
        String stateID = ids.component1();
        String countyID = ids.component2();

        HttpRequest buildACSApiRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.census.gov/data/2021/acs/acs1/" +
                                "subject/variables?get=NAME,S2802_C03_022E&for=county:" + countyID + "&in=state:" + stateID +
                                "&key=" + key))
                        .GET()
                        .build();

        HttpResponse<String> sentACSApiResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildACSApiRequest, HttpResponse.BodyHandlers.ofString());

        return sentACSApiResponse.body();
    }

    public static LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}

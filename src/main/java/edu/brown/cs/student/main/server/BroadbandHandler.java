package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.student.main.datasource.BroadbandDataSource;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {
    BroadbandDataSource dataSource;

    public BroadbandHandler(BroadbandDataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Object handle(Request request, Response response) {
        String state = request.queryParams("state");
        String county = request.queryParams("county");
        String key = request.queryParams("key");

        Map<String, Object> responseMap = new HashMap<>();
        try {
            String responseString = this.dataSource.sendRequest(state, county, key);
            List<List<String>> deserializedResponse = BroadbandUtilities.deserializeResponse(responseString);
            responseMap.put("query_time", BroadbandDataSource.getTime());
            responseMap.put("data", BroadbandUtilities.processResponseList(deserializedResponse));

        } catch (Exception e) {
            e.printStackTrace();
            return new ACSFailureResponse(e.toString()).serialize();
        }
        return new ACSSuccessResponse(responseMap).serialize();
    }

    public record ACSSuccessResponse(String response_type, Map<String, Object> responseMap) {
        public ACSSuccessResponse(Map<String, Object> responseMap) {
            this("success", responseMap);
        }
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            // Initialize Moshi which takes in this class and returns it as JSON!
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<BroadbandHandler.ACSSuccessResponse> adapter =
                    moshi.adapter(BroadbandHandler.ACSSuccessResponse.class);
            return adapter.toJson(this);
        }
    }

    /** Response object to send if someone requested soup from an empty Menu */
    public record ACSFailureResponse(String response_type, String error_msg) {
        public ACSFailureResponse(String error_msg) {
            this("Exception", error_msg);
        }

        /**
         * @return this response, serialized as Json
         *
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(BroadbandHandler.ACSFailureResponse.class).toJson(this);
        }
    }
}

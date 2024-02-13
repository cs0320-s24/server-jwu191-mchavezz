package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.activity.Activity;
import edu.brown.cs.student.main.activity.ActivityAPIUtilities;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoadHandler implements Route {
    public LoadHandler(DataSource data){}
    public Object handle(Request request, Response response) {
        Set<String> params = request.queryParams();
        String filePath = request.queryParams("filePath");

        Map<String, Object> responseMap = new HashMap<>();

        return responseMap;
    }


}

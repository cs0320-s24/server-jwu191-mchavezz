package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.datasource.DataSource;
import spark.Request;
import spark.Response;
import spark.Route;

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

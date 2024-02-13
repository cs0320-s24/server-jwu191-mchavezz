package edu.brown.cs.student.main.server;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewHandler implements Route {
    public ViewHandler(DataSource data){}
    public Object handle(Request request, Response response) {
        Set<String> params = request.queryParams();
        String participants = request.queryParams("participants");

        Map<String, Object> responseMap = new HashMap<>();

        return responseMap;
    }
}

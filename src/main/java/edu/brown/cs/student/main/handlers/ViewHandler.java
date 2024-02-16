package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.DataSource;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewHandler implements Route {

  DataSource<CSVSearcher> dataSource;

  public ViewHandler(DataSource<CSVSearcher> dataSource) {
    this.dataSource = dataSource;
  }

  public Object handle(Request request, Response response) {
    if (!dataSource.dataLoaded()) {
      return new ViewFailureResponse("error: Data has not been loaded").serialize();
    }
    Map<String, Object> responseMap = new HashMap<>();

    responseMap.put("data", dataSource.getData().getData());

    return new ViewSuccessResponse(responseMap).serialize();
  }

  public record ViewSuccessResponse(String response_type, Map<String, Object> responseMap) {
    public ViewSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      // Initialize Moshi which takes in this class and returns it as JSON!
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<ViewHandler.ViewSuccessResponse> adapter =
          moshi.adapter(ViewHandler.ViewSuccessResponse.class);
      return adapter.toJson(this);
    }
  }

  public record ViewFailureResponse(String response_type, String error_msg) {
    public ViewFailureResponse(String error_msg) {
      this("Exception", error_msg);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ViewHandler.ViewFailureResponse.class).toJson(this);
    }
  }
}

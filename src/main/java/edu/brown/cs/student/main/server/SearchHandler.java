package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  DataSource<CSVSearcher> dataSource;

  public SearchHandler(DataSource<CSVSearcher> dataSource) {
    this.dataSource = dataSource;
  }

  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    if (!this.dataSource.dataLoaded()) {
      responseMap.put("result", "failure");
      return new SearchFailureResponse("error: Data has not been loaded").serialize();
    }
    Set<String> params = request.queryParams();
    String value = request.queryParams("value");
    String column = request.queryParams("column");
    CSVSearcher searcher = this.dataSource.getData();
    boolean searchSuccess = false;

    if (column == null) {
      searchSuccess = searcher.search(value);
    } else if (column.matches("(0|[1-9]\\d*)")) {
      searchSuccess = searcher.search(Integer.parseInt(column), value);
    } else {
      searchSuccess = searcher.search(column, value);
    }

    if (!searchSuccess) {
      return new SearchFailureResponse("error: Search operation was unsuccessful").serialize();
    }

    responseMap.put("result", searcher.getLastSearchResult());

    return new SearchSuccessResponse(responseMap).serialize();
  }

  /** Response object to send, containing a soup with certain ingredients in it */
  public record SearchSuccessResponse(String response_type, Map<String, Object> responseMap) {
    public SearchSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      // Initialize Moshi which takes in this class and returns it as JSON!
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchHandler.SearchSuccessResponse> adapter =
          moshi.adapter(SearchHandler.SearchSuccessResponse.class);
      return adapter.toJson(this);
    }
  }

  /** Response object to send if someone requested soup from an empty Menu */
  public record SearchFailureResponse(String response_type, String error_msg) {
    public SearchFailureResponse(String error_msg) {
      this("Exception", error_msg);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchHandler.SearchFailureResponse.class).toJson(this);
    }
  }
}

package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.csv.comparators.DefaultComparator;
import edu.brown.cs.student.main.csv.creators.DefaultCreator;
import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadHandler implements Route {
  private final DataSource<CSVSearcher> dataSource;

  public LoadHandler(DataSource<CSVSearcher> dataSource) {
    this.dataSource = dataSource;
  }

  public Object handle(Request request, Response response) {
    Path dataDirectory = Paths.get("TODO");
    Set<String> params = request.queryParams();
    String filePath = request.queryParams("filePath");

    Map<String, Object> responseMap = new HashMap<>();
    DefaultCreator creator = new DefaultCreator();
    DefaultComparator comparator = new DefaultComparator();
    CSVSearcher searcher = new CSVSearcher(filePath, true, comparator, dataDirectory, false);
    dataSource.setData(searcher);
    if (searcher.fileSuccessfullyParsed()) {
      responseMap.put("result", "success");
    } else {
      responseMap.put("result", "failure");
    }

    return responseMap;
  }
}

package edu.brown.cs.student.main.handlers;

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
  private final Path dataDirectory;

  public LoadHandler(DataSource<CSVSearcher> dataSource, Path dataDirectory) {
    this.dataSource = dataSource;
    this.dataDirectory = dataDirectory;
  }

  public Object handle(Request request, Response response) {
    Set<String> params = request.queryParams();
    String filePath = request.queryParams("filePath");

    Map<String, Object> responseMap = new HashMap<>();
    DefaultCreator creator = new DefaultCreator();
    DefaultComparator comparator = new DefaultComparator();
    CSVSearcher searcher = new CSVSearcher(filePath, true, comparator, this.dataDirectory, false);
    this.dataSource.setData(searcher);
    if (searcher.fileSuccessfullyParsed()) {
      responseMap.put("result", "success");
    } else {
      responseMap.put("result", "failure");
    }

    return responseMap;
  }
}

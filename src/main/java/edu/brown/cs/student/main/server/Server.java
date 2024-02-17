package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.BroadbandDataSource;
import edu.brown.cs.student.main.datasource.DataSource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import spark.Spark;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 * <p>Notice that the OrderHandler takes in a state (menu) that can be shared if we extended the
 * restaurant They need to share state (a menu). This would be a great opportunity to use dependency
 * injection. If we needed more endpoints, more functionality classes, etc. we could make sure they
 * all had the same shared state.
 */
public class Server {
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Sets up data needed for the OrderHandler. You will likely not read from local
    // JSON in this sprint.
    DataSource<CSVSearcher> dataSource = new DataSource<>();
    try {
      BroadbandDataSource broadbandDataSource = new BroadbandDataSource();
      Path dataDirectory = Paths.get("C:\\Repos\\GitHub\\BrownCS\\CSCI_0320\\server-jwu191-mchavezz\\data");
      // Setting up the handler for the GET /order and /activity endpoints
      Spark.get("load", new LoadHandler(dataSource, dataDirectory));
      Spark.get("search", new SearchHandler(dataSource));
      Spark.get("view", new ViewHandler(dataSource));
      Spark.get("broadband", new BroadbandHandler(broadbandDataSource));
      Spark.init();
      Spark.awaitInitialization();

      // Notice this link alone leads to a 404... Why is that?
      System.out.println("Server started at http://localhost:" + port);
    }
    catch(Exception e) {
      System.out.println(e.toString());
    }
  }
}

package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.DataSource;
import edu.brown.cs.student.main.server.LoadHandler;
import edu.brown.cs.student.main.server.SearchHandler;
import edu.brown.cs.student.main.server.ViewHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestAPIHandlers {
    @BeforeAll
    public static void setUp() {
        Spark.port(0); // Set port to 0 to let Spark choose an available port
        Spark.awaitInitialization();
    }

    @BeforeEach
    public void beforeEach() {
        Spark.init();
    }

    @AfterEach
    public void afterEach() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void testLoadHandlerSuccess() throws IOException {
        // Create a mock DataSource
        DataSource<CSVSearcher> dataSource = new DataSource<>();
        LoadHandler loadHandler = new LoadHandler(dataSource);

        // Mock request with a file path
        Request request = new MockRequest();
        request.queryParams().add("filePath", "/path/to/file.csv");

        // Mock response
        Response response = new MockResponse();

        // Handle the request
        Object result = loadHandler.handle(request, response);

        // Verify the response
        assertEquals("{\"result\":\"success\"}", result.toString());
    }

    @Test
    public void testSearchHandlerSuccess() throws IOException {
        // Create a mock DataSource with data loaded
        DataSource<CSVSearcher> dataSource = new DataSource<>();
        dataSource.setData(new CSVSearcher("/path/to/file.csv", true, null, null, false));
        SearchHandler searchHandler = new SearchHandler(dataSource);

        // Mock request with search parameters
        Request request = new MockRequest();
        request.queryParams().add("column", "1");
        request.queryParams().add("value", "searchValue");

        // Mock response
        Response response = new MockResponse();

        // Handle the request
        Object result = searchHandler.handle(request, response);

        // Verify the response
        assertEquals("{\"response_type\":\"Success\",\"result\":true}", result.toString());
    }

    @Test
    public void testViewHandlerSuccess() throws IOException {
        // Create a mock DataSource with data loaded
        DataSource<CSVSearcher> dataSource = new DataSource<>();
        dataSource.setData(new CSVSearcher("/path/to/file.csv", true, null, null, false));
        ViewHandler viewHandler = new ViewHandler(dataSource);

        // Mock request
        Request request = new MockRequest();

        // Mock response
        Response response = new MockResponse();

        // Handle the request
        Object result = viewHandler.handle(request, response);

        // Verify the response
        assertEquals("{\"response_type\":\"Success\",\"result\":null}", result.toString());
    }

    // Mock Request class
    static class MockRequest extends Request {
        private final Map<String, String> queryParams = new HashMap<>();

        @Override
        public String queryParams(String queryParam) {
            return queryParams.get(queryParam);
        }

        @Override
        public Set<String> queryParams() {
            return queryParams.keySet();
        }

        // Implement other methods of Request interface as needed
    }

    // Mock Response class
    static class MockResponse extends Response {
        private final OutputStream outputStream = new OutputStream() {
            private final StringBuilder response = new StringBuilder();

            @Override
            public void write(int b) {
                response.append((char) b);
            }

            @Override
            public String toString() {
                return response.toString();
            }
        };

        @Override
        public OutputStream body() {
            return outputStream;
        }

        // Implement other methods of Response interface as needed
    }
}

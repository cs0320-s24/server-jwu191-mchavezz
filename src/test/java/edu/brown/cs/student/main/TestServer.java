package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csv.searcher.CSVSearcher;
import edu.brown.cs.student.main.datasource.BroadbandDataSource;
import edu.brown.cs.student.main.datasource.DataSource;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.server.BroadbandAccessData;
import edu.brown.cs.student.main.server.BroadbandAccessDataCreator;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestServer {

    @Test
    void testBroadbandDataSource() {
        try {
            // Test constructor and populateIDMaps method
            BroadbandDataSource dataSource = new BroadbandDataSource();
            dataSource.populateIDMaps();
            assertFalse(dataSource.stateIDMap.isEmpty());
            assertFalse(dataSource.countyIDMap.isEmpty());

            // Test retrieveIDs method
            dataSource.stateIDMap.put("California", "06");
            dataSource.countyIDMap.put("Los Angeles", List.of("037", "06"));
            assertEquals("06", dataSource.retrieveIDs("California", "Los Angeles").component1());
            assertEquals("037", dataSource.retrieveIDs("California", "Los Angeles").component2());

            // Test sendRequest method (mocked)
            HttpClient mockedClient = mock(HttpClient.class);
            HttpResponse<String> mockedResponse = mock(HttpResponse.class);
            when(mockedResponse.body()).thenReturn("Mocked Response Body");
            when(mockedClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.class)))
                    .thenReturn(mockedResponse);
            dataSource.setHttpClient(mockedClient);
            String response = dataSource.sendRequest("California", "Los Angeles", "your_api_key");
            assertNotNull(response);
            assertEquals("Mocked Response Body", response);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testDataSource() {
        DataSource<String> dataSource = new DataSource<>();
        assertFalse(dataSource.dataLoaded());

        dataSource.setData("Test Data");
        assertTrue(dataSource.dataLoaded());
        assertEquals("Test Data", dataSource.getData());
    }

    @Test
    void testBroadbandHandler() {
        // Test BroadbandHandler class
        // You can mock the dependencies and test the handle method with different scenarios
    }

    @Test
    void testHandle_Success() {
        DataSource<CSVSearcher> dataSource = mock(DataSource.class);
        LoadHandler loadHandler = new LoadHandler(dataSource, Paths.get("test_data"));

        // Mock request and response objects
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Set up expectations
        when(request.queryParams("filePath")).thenReturn("test.csv");
        when(dataSource.setData(any())).thenReturn(true);

        // Call the handler
        loadHandler.handle(request, response);

        // Verify response status code
        verify(response).status(200);
    }

    @Test
    void testHandle_Failure() {
        DataSource<CSVSearcher> dataSource = mock(DataSource.class);
        LoadHandler loadHandler = new LoadHandler(dataSource, Paths.get("test_data"));

        // Mock request and response objects
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Set up expectations
        when(request.queryParams("filePath")).thenReturn("invalid_file.csv");
        when(dataSource.setData(any())).thenReturn(false);

        // Call the handler
        loadHandler.handle(request, response);

        // Verify response status code
        verify(response).status(500);
    }

    @Test
    void testHandle_Success_SearchHandler() throws IOException {

        Path testCsvFile = createTestCsvFile();


        DataSource<CSVSearcher> dataSource = new DataSource<>();
        CSVSearcher searcher = new CSVSearcher(testCsvFile.toString(), true, null, null, false);
        dataSource.setData(searcher);

        SearchHandler searchHandler = new SearchHandler(dataSource);

        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.queryParams("value")).thenReturn("search_value");
        when(request.queryParams("column")).thenReturn("search_column");

        when(dataSource.dataLoaded()).thenReturn(true);
        when(searcher.search("search_column", "search_value")).thenReturn(true);
        when(searcher.getLastSearchResult()).thenReturn("search_result");

        searchHandler.handle(request, response);

        // Verify response status code
        verify(response).status(200);
        // Verify response body
        verify(response).body("{\"result\":\"search_result\"}");

        Files.deleteIfExists(testCsvFile);
    }

    // Helper method to create a temporary test CSV file
    private Path createTestCsvFile() throws IOException {

        Path tempDirectory = Files.createTempDirectory("test_files");
        Path tempCsvFilePath = tempDirectory.resolve("test.csv");

        URL url = new URL("https://docs.google.com/spreadsheets/d/e/2PACX-1vQZPABNye4lYIfKbYzRR7zjj4dQcZCh-C0AOpQFWOEhIdhHjwOQ8oKzgrjagJg6J0n8Av6v7u0PVyLF/pub?output=csv");
        Files.copy(url.openStream(), tempCsvFilePath, StandardCopyOption.REPLACE_EXISTING);

        return tempCsvFilePath;
    }

    @Test
    void testViewHandler() {
    }

    @Test
    void testBroadbandAccessApp() {
    }

    @Test
    void testBroadbandAccessDataCreator() {
        // Test BroadbandAccessDataCreator class
        BroadbandAccessDataCreator creator = new BroadbandAccessDataCreator();

        // Test create method with valid input
        try {
            List<String> validRow = List.of("California", "Los Angeles", "0.85");
            BroadbandAccessData data = creator.create(validRow);
            assertNotNull(data);
            assertEquals("California", data.getState());
            assertEquals("Los Angeles", data.getCounty());
            assertEquals(0.85, data.getBroadbandAccessPercentage());
        } catch (Exception e) {
            fail("Exception occurred for valid input: " + e.getMessage());
        }

        // Test create method with invalid input
        List<String> invalidRow = List.of("California", "Los Angeles");
        assertThrows(Exception.class, () -> creator.create(invalidRow));
    }
}


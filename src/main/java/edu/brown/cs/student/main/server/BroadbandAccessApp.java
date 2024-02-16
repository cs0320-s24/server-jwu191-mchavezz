package edu.brown.cs.student.main.server;

import static spark.Spark.get;
import static spark.Spark.port;

import com.google.gson.Gson;
import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.parser.CSVParser;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class BroadbandAccessApp {
    public static void main(String[] args) {
        port(8080); // setting desired port

        get("/broadband-access", (req, res) -> {
            String state = req.queryParams("state");
            String county = req.queryParams("county");

            // making a request to ACS API
            List<BroadbandAccessData> broadbandData = makeACSRequest(state, county);

            // creating response object
            BroadbandAccessResponse response = new BroadbandAccessResponse();
            response.setBroadbandData(broadbandData);
            response.setState(state);
            response.setCounty(county);
            response.setDateTime(LocalDateTime.now());

            res.type("application/json");
            return new Gson().toJson(response);
        });
    }

    private static List<BroadbandAccessData> makeACSRequest(String state, String county) throws IOException, FactoryFailureException {
        // Read the CSV file
        try (FileReader fileReader = new FileReader("")) {
            // Define the creator for creating objects from CSV rows
            CreatorFromRow<BroadbandAccessData> creator = new BroadbandAccessDataCreator();

            // Create a CSVParser instance
            CSVParser<BroadbandAccessData> csvParser = new CSVParser<BroadbandAccessData>(fileReader, creator);

            // Return the parsed data
            return csvParser.getData();
        }
    }
}

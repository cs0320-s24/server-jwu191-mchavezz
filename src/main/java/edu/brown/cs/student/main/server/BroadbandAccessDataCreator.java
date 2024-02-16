package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.csv.creators.CreatorFromRow;
import edu.brown.cs.student.main.csv.exceptions.FactoryFailureException;
import java.util.List;

public class BroadbandAccessDataCreator implements CreatorFromRow<BroadbandAccessData> {

    @Override
    public BroadbandAccessData create(List<String> row) throws FactoryFailureException {
        if (row.size() != 3) {
            throw new FactoryFailureException("Invalid row format");
        }
        // Assuming the structure of the row is: State, County, BroadbandAccessPercentage
        String state = row.get(0);
        String county = row.get(1);
        double broadbandAccessPercentage = Double.parseDouble(row.get(2));

        // Create and return a new BroadbandAccessData object
        return new BroadbandAccessData(state, county, broadbandAccessPercentage);
    }
}

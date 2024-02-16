package edu.brown.cs.student.main.utilities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.activity.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BroadbandUtilities {
    public static List<List<String>> deserializeResponse(String json) {
        try {
            // Initializes Moshi
            Moshi moshi = new Moshi.Builder().build();

            // Initializes an adapter to an Activity class then uses it to parse the JSON.
            JsonAdapter<List<List<String>>> adapter = moshi.adapter(Types.newParameterizedType(List.class, List.class, String.class));

            return adapter.fromJson(json);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<List<String>>();
        }
    }

    public static List<List<String>> processResponseList(List<List<String>> response) {
        response.get(0).set(1, "percentage of households with broadband internet");
        for (List<String> innerList : response) {
            // Truncate the inner list if its size is greater than 2
            if (innerList.size() > 2) {
                innerList.subList(2, innerList.size()).clear();
            }
        }
        return response;
    }
}

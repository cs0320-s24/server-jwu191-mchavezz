package edu.brown.cs.student.main.datasource;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    List<String> data;
    public DataSource() {
        data = new ArrayList<>();
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData(List<String> data) {
        return this.data;
    }
}

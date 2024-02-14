package edu.brown.cs.student.main.datasource;

public class DataSource<T> {
  T data;
  boolean dataLoaded;

  public DataSource() {
    dataLoaded = false;
  }

  public void setData(T data) {
    this.data = data;
  }

  public T getData() {
    return this.data;
  }

  public boolean dataLoaded() {
    return this.dataLoaded;
  }
}

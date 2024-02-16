package edu.brown.cs.student.main.datasource;

public class DataSource<T> {
  T data;
  boolean dataLoaded;

  public DataSource() {
    this.dataLoaded = false;
  }

  public void setData(T data) {
    this.data = data;
    this.dataLoaded = true;
  }

  public T getData() {
    return this.data;
  }

  public boolean dataLoaded() {
    return this.dataLoaded;
  }
}

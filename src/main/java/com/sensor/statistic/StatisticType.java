package com.sensor.statistic;

public enum StatisticType {
  SUM("sum"),
  MAX("max"),
  MIN("min"),
  AVG("avg");

  private String type;

  StatisticType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public String toString() {
    return this.type;
  }
}

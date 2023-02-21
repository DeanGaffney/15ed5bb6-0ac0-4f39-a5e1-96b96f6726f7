package com.sensor.metric;

public enum MetricType {
  TEMPERATURE("temperature"),
  WIND_SPEED("windSpeed"),
  HUMIDITY("humidity");

  private String type;

  MetricType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public String toString() {
    return this.type;
  }

}

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

  public static MetricType typeToValue(String type) {
    if (type.equals("temperature")) {
      return MetricType.TEMPERATURE;
    }

    if (type.equals("windSpeed")) {
      return MetricType.WIND_SPEED;
    }

    if (type.equals("humidity")) {
      return MetricType.HUMIDITY;
    }

    throw new IllegalArgumentException(type + " is not supported");

  }
}

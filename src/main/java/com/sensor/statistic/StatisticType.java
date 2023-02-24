package com.sensor.statistic;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

  public static BigDecimal convertToBigDecimal(Object value, StatisticType type) {
    if (type == StatisticType.AVG || type == StatisticType.SUM) {
      BigDecimal bigDecimal = ((BigDecimal) value);
      bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
      return bigDecimal;
    }

    return new BigDecimal((Double) value);
  }
}

package com.sensor.metric;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class SensorMetricQueryResult {

  private Long sensorId;

  private MetricType metricType;

  private BigDecimal statisticValue;

  public SensorMetricQueryResult(Long sensorId, MetricType metricType, BigDecimal statisticValue) {
    this.sensorId = sensorId;
    this.metricType = metricType;
    this.statisticValue = statisticValue;
  }

  public Long getSensorId() {
    return this.sensorId;
  }

  public MetricType getMetricType() {
    return this.metricType;
  }

  public BigDecimal getStatisticValue() {
    return this.statisticValue.setScale(2, RoundingMode.HALF_EVEN);
  }

  public Map<String, Object> toMap() {
    Map<String, Object> summary = new HashMap<String, Object>();

    summary.put("metricType", MetricType.typeToValue(metricType.getType()));
    summary.put("value", this.getStatisticValue());

    return summary;
  }

  public String toString() {
    return "[sensorId = " + this.getSensorId() + ", " +
        "metricType = " + this.getMetricType().getType() + ", " +
        "statisticValue = " + this.getStatisticValue() + "]";
  }

}

package com.sensor.metric;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Metric {
  private MetricType metricType;

  @Column(precision = 10, scale = 2)
  private BigDecimal value;

  public Metric() {
  }

  public Metric(MetricType metricType, BigDecimal value) {
    this.metricType = metricType;
    this.value = value;
  }

  public MetricType getMetricType() {
    return this.metricType;
  }

  public BigDecimal getValue() {
    return this.value.setScale(2, RoundingMode.HALF_EVEN);
  }
}

package com.sensor.metric;

import javax.persistence.Embeddable;


@Embeddable
public class Metric {
  private MetricType metricType;

  private double value;

  public Metric(){}

  public Metric(MetricType metricType, double value) {
    this.metricType = metricType;
    this.value = value;
  }

  public MetricType getMetricType () {
    return this.metricType;
  }

  public double getValue() {
    return this.value;
  }
}

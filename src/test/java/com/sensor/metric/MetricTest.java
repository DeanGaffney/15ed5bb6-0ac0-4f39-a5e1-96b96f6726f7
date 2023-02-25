package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MetricTest {

  @Test
  public void shouldNotErrorUsingDefaultConstructor() {
     new Metric();
  }

  @Test
  public void shouldReturnMetricType() {
    double metricValue = 2.5;
    Metric metric = new Metric(MetricType.HUMIDITY, metricValue);
    assertEquals(metric.getMetricType(), MetricType.HUMIDITY);
  }

  @Test
  public void shouldReturnMetricValue() {
    double metricValue = 2.5;
    Metric metric = new Metric(MetricType.HUMIDITY, metricValue);
    assertEquals(metric.getValue(), metricValue);
  }
}

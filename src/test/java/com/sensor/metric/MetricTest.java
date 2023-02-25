package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class MetricTest {

  @Test
  public void shouldNotErrorUsingDefaultConstructor() {
    new Metric();
  }

  @Test
  public void shouldReturnMetricType() {
    BigDecimal metricValue = new BigDecimal(2.5);
    Metric metric = new Metric(MetricType.HUMIDITY, metricValue);
    assertEquals(metric.getMetricType(), MetricType.HUMIDITY);
  }

  @Test
  public void shouldReturnMetricValue() {
    BigDecimal metricValue = new BigDecimal(2.50);
    Metric metric = new Metric(MetricType.HUMIDITY, metricValue);
    assertEquals(metric.getValue().setScale(2, RoundingMode.FLOOR), metricValue.setScale(2, RoundingMode.FLOOR));
  }
}

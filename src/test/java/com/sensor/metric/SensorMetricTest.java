package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class SensorMetricTest {

  @Test
  public void shouldCreateaSensorMetric() {
    Long sensorId = 1l;
    Metric metric = new Metric(MetricType.HUMIDITY, new BigDecimal(2.5));
    LocalDateTime createdDate = LocalDateTime.of(2023, 2, 25, 0, 0);

    SensorMetric sensorMetric = new SensorMetric(sensorId, metric, createdDate);

    assertEquals(sensorMetric.getMetric(), metric);
    assertEquals(sensorMetric.getCreatedDate(), createdDate);
    assertEquals(sensorMetric.getSensorId(), sensorId);
    // generated by persistence layer
    assertEquals(sensorMetric.getId(), null);
  }
}

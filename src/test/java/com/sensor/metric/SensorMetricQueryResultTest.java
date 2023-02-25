package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SensorMetricQueryResultTest {

  @Test
  public void shouldCreateSensorMetricQueryResult() {
    Long sensorId = 1l;
    MetricType metricType = MetricType.TEMPERATURE;
    BigDecimal statisticValue = new BigDecimal(2.5);

    SensorMetricQueryResult queryResult = new SensorMetricQueryResult(sensorId, metricType, statisticValue);

    assertEquals(queryResult.getSensorId(), sensorId);
    assertEquals(queryResult.getMetricType(), metricType);
    assertEquals(queryResult.getStatisticValue(), statisticValue);
  }

  @Test
  public void shouldConvertToMap() {
    Long sensorId = 1l;
    MetricType metricType = MetricType.TEMPERATURE;
    BigDecimal statisticValue = new BigDecimal(2.5);

    SensorMetricQueryResult queryResult = new SensorMetricQueryResult(sensorId, metricType, statisticValue);

    Map<String, Object> resultAsMap = queryResult.toMap();
    assertEquals(resultAsMap.get("metricType"), metricType);
    assertEquals(resultAsMap.get("value"), statisticValue);
  }

  @Test
  public void shouldCreateToString() {
    Long sensorId = 1l;
    MetricType metricType = MetricType.TEMPERATURE;
    BigDecimal statisticValue = new BigDecimal(2.5);

    SensorMetricQueryResult queryResult = new SensorMetricQueryResult(sensorId, metricType, statisticValue);

    assertEquals(queryResult.toString(), "[sensorId = " + sensorId + ", " +
        "metricType = " + metricType.getType() + ", " +
        "statisticValue = " + statisticValue + "]");
    ;

  }
}

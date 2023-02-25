package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MetricTypeTest {

  @Test
  public void shouldContainExpectedValues() {
    assertArrayEquals(MetricType.values(),
        new MetricType[] { MetricType.TEMPERATURE, MetricType.WIND_SPEED, MetricType.HUMIDITY });
  }

  @Test
  public void shouldGetType() {
    assertEquals(MetricType.TEMPERATURE.getType(), "temperature");
    assertEquals(MetricType.WIND_SPEED.getType(), "windSpeed");
    assertEquals(MetricType.HUMIDITY.getType(), "humidity");
  }

  @Test
  public void shouldConvertTypeToValue() {
    assertEquals(MetricType.typeToValue("temperature"), MetricType.TEMPERATURE);
    assertEquals(MetricType.typeToValue("windSpeed"), MetricType.WIND_SPEED);
    assertEquals(MetricType.typeToValue("humidity"), MetricType.HUMIDITY);
  }

  @Test
  public void shouldConvertTypeToValueAndErrorForInvalidType() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        MetricType.typeToValue("invalid-type");
    });

    assertEquals(exception.getMessage(), "invalid-type is not supported");
  }

  @Test
  public void shouldReturnExpectedToString() {
    assertEquals(MetricType.HUMIDITY.toString(), "humidity");
  }
}

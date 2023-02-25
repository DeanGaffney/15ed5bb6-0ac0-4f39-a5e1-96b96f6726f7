package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MetricTypeConverterTest {

  @Test
  public void shouldConvertToDatabseColumn() {
    MetricTypeConverter converter = new MetricTypeConverter();
    String convertedValue = converter.convertToDatabaseColumn(MetricType.HUMIDITY);

    assertEquals(convertedValue, MetricType.HUMIDITY.getType());
  }

  @Test
  public void shouldReturnNullWhenConvertingToDatabaseColumWhenNullIsProvided() {
    MetricTypeConverter converter = new MetricTypeConverter();
    String convertedValue = converter.convertToDatabaseColumn(null);

    assertEquals(convertedValue, null);
  }

  @Test
  public void shouldConvertToEntityAttribute() {
    MetricTypeConverter converter = new MetricTypeConverter();
    MetricType metricType = converter.convertToEntityAttribute("humidity");

    assertEquals(metricType, MetricType.HUMIDITY);
  }

  @Test
  public void shouldReturnNullWhenConvertingToEntityAttibuteWhenNullIsProvided() {
    MetricTypeConverter converter = new MetricTypeConverter();
    MetricType metricType = converter.convertToEntityAttribute(null);

    assertEquals(metricType, null);
  }

  @Test
  public void shouldThrowExceptionWhenConvertingToentityAttributeForUnsupportedType() {
    MetricTypeConverter converter = new MetricTypeConverter();
    assertThrows(IllegalArgumentException.class, () -> {
       converter.convertToEntityAttribute("invalid");
    });
  }
}

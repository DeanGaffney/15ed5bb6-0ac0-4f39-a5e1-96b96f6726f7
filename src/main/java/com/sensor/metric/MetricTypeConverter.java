package com.sensor.metric;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MetricTypeConverter implements AttributeConverter<MetricType, String> {
 
    @Override
    public String convertToDatabaseColumn(MetricType metricType) {
        if (metricType == null) {
            return null;
        }
        return metricType.getType();
    }

    @Override
    public MetricType convertToEntityAttribute(String type) {
        if (type == null) {
            return null;
        }

        return Stream.of(MetricType.values())
          .filter(c -> c.getType().equals(type))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}

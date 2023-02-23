package com.sensor.metric;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SensorMetricQueryResult {

  @Id
  private Long sensorId;

  private MetricType metricType;

  private Double sum;

  private Double min;

  private Double max;

  private Double avg;

  public SensorMetricQueryResult() {

  }

  public Long getSensorId() {
    return this.sensorId;
  }

  public MetricType getMetricType() {
    return this.metricType;
  }

  public Double getSum() {
    return this.sum;
  }

  public Double getMax() {
    return this.max;
  }

  public Double getMin() {
    return this.min;
  }

  public Double getAvg() {
    return this.avg;
  }

}

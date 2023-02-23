package com.sensor.metric;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SensorMetric {

  private @Id @GeneratedValue Long id;

  @Column(name = "sensor_id")
  private Long sensorId;

  // Metric is separated out into its own class, but there is no need for a
  // separate table
  // the columns for metric should be added to the senor_metric table
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "metricType", column = @Column(name = "metric_type")),
      @AttributeOverride(name = "value", column = @Column(name = "metric_value"))
  })
  private Metric metric;

  @Column(name = "created_date")
  private LocalDateTime createdDate;

  public SensorMetric(Long sensorId, Metric metric, LocalDateTime createdDate) {
    this.sensorId = sensorId;
    this.metric = metric;
    this.createdDate = createdDate;
  }

  public Long getId() {
    return this.id;
  }

  public Long getSensorId() {
    return this.sensorId;
  }

  public Metric getMetric() {
    return this.metric;
  }

  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }
}

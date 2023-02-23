package com.sensor.metric;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SensorMetricRepository {

  @Autowired
  private final EntityManager entityManager;

  public SensorMetricRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void querySensorMetrics(SensorMetricQuery query) {
    SensorMetricQueryBuilder builder = new SensorMetricQueryBuilder();

    builder.select(Arrays.asList("sensor_id as sensorId", "metric_type as metricType,"))
        .selectWithStatistics("metric_value", query.getStatistics())
        .from("sensor_metric")
        .where();

    if (query.containsValidDateRange()) {
      builder.dateBetween("created_date", query.getFromDate().get(), query.getEndDate().get())
          .and();
    }

    if (query.containsSensorIds()) {
      builder.whereValueIn("sensor_id", query.getSensorIds())
          .and();
    }

    builder.metricsMatch("metric_type", query.getMetrics());

    String querytoExecute = builder
        .groupBy(Arrays.asList("sensor_id", "metric_type"))
        .build();

    @SuppressWarnings("unchecked")
    List<SensorMetricQueryResult> result = entityManager.createNativeQuery(querytoExecute, SensorMetricQueryResult.class).getResultList();

    result.forEach(r -> {
      System.out.println("Sensor id " + r.getSensorId());
      System.out.println("Metric type" + r.getMetricType());
      System.out.println("Max " + r.getMax());
      System.out.println("Min " + r.getMin());
      System.out.println("Sum" + r.getSum());
      System.out.println("Avg" + r.getAvg());
    });
  }
}

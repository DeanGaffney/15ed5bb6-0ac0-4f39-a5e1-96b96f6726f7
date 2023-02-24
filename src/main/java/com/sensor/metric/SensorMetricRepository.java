package com.sensor.metric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sensor.result.Result;
import com.sensor.statistic.StatisticType;

@Repository
public class SensorMetricRepository {

  @Autowired
  private final EntityManager entityManager;

  public SensorMetricRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Result<List<SensorMetricQueryResult>> querySensorMetrics(SensorMetricQuery query) {
    SensorMetricQueryBuilder builder = new SensorMetricQueryBuilder();

    try {
      builder.select(Arrays.asList("sensor_id", "metric_type,"))
          .selectWithStatistic("metric_value", query.getStatistic())
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

      // warning is unavoidable when running a native query and casting to a POJO
      @SuppressWarnings("unchecked")
      List<Object[]> records = entityManager
          .createNativeQuery(querytoExecute)
          .getResultList();

      List<SensorMetricQueryResult> result = records.stream()
          .map(record -> {
            BigInteger sensorId = (BigInteger) record[0];
            MetricType metricType = MetricType.typeToValue((String) record[1]);
            BigDecimal value = StatisticType.convertToBigDecimal(record[2], query.getStatistic());
            return new SensorMetricQueryResult(sensorId.longValue(), metricType, value);
          })
          .collect(Collectors.toList());

      return Result.ok(result);
    } catch (Exception e) {
      // TODO add logging here
      return Result.error(new RuntimeException("Failed to execute query"));
    }
  }
}

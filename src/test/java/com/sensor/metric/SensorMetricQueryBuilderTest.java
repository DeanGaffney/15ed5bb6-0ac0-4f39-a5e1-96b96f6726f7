package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.sensor.statistic.StatisticType;

public class SensorMetricQueryBuilderTest {

  @Test
  public void shouldBuildQuery() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<List<StatisticType>> statistics = Optional.of(
        Arrays.asList(new StatisticType[] { StatisticType.AVG, StatisticType.SUM }));

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistics, optionalFromDate,
        optionalEndDate);

    SensorMetricQueryBuilder builder = new SensorMetricQueryBuilder();

    String dbQuery = builder.select(Arrays.asList("sensor_id", "metric_type,"))
        .selectWithStatistics("metric_type", query.getStatistics())
        .from("sensor_metric")
        .where()
        .dateBetween("created_date", "fromDate", "endDate")
        .and()
        .whereValueIn("sensor_id", "sensorIds")
        .and()
        .metricsMatch("metric_type", query.getMetrics())
        .groupBy(Arrays.asList("sensor_id", "metric_type"))
        .build();

    System.out.println(dbQuery);

    assertEquals("SELECT sensor_id, metric_type, SUM(metric_type) as sum," +
        " MAX(metric_type) as max," +
        " MIN(metric_type) as min, " + "AVERAGE(metric_type) as average " +
        "FROM sensor_metric " +
        "WHERE created_date BETWEEN :fromDate AND :endDate " +
        "AND sensor_id IN :sensorIds AND " +
        "metric_type IN ('temperature', 'windSpeed', 'humidity') " +
        "GROUP BY sensor_id, metric_type;", dbQuery);
  }
}

package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.sensor.result.Result;
import com.sensor.statistic.StatisticType;

public class SensorMetricQueryTest {

  @Test
  public void shouldCreateSensorMetricQuery() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistic = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.getMetrics(), metricTypes.get());
    assertEquals(query.getStatistic(), statistic.get());
    assertEquals(query.getSensorIds(), sensorIds.get());
    assertEquals(query.getFromDate().get(), fromDate);
    assertEquals(query.getEndDate().get(), endDate);
  }

  @Test
  public void shouldReturnTrueIfQueryContainsSensorIds() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistic = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.containsSensorIds(), true);
  }

  @Test
  public void shouldReturnFalseIfThereAreNoSensorIds() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistic = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.containsSensorIds(), false);
  }

  @Test
  public void shouldReturnEmptyListIfNoSensorIdsAreProvided(){  
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistic = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.getSensorIds(), Collections.emptyList());
  }

  @Test
  public void shouldReturnAllMetricTypesIfNoneAreProvided() {
    Optional<List<MetricType>> metricTypes = Optional.ofNullable(null);

    Optional<StatisticType> statistic = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.getMetrics(), Arrays.asList(MetricType.values()));
  }

  @Test
  public void shouldReturnAverageStatisticIfNoStatisticIsProvided() {
    Optional<List<MetricType>> metricTypes = Optional.ofNullable(null);

    Optional<StatisticType> statistic = Optional.ofNullable(null);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    assertEquals(query.getStatistic(), StatisticType.AVG);
  }

  @Test
  public void shouldReturnAnErrorResultIfNotDateRangeIsProvided() {
    Optional<List<MetricType>> metricTypes = Optional.ofNullable(null);

    Optional<StatisticType> statistic = Optional.ofNullable(null);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    Optional<LocalDateTime> optionalFromDate = Optional.ofNullable(null);
    Optional<LocalDateTime> optionalEndDate = Optional.ofNullable(null);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    Result<SensorMetricQuery> validateResult = query.validate();

    assertEquals(validateResult.isNotOk(), true);
    assertEquals(validateResult.getExceptionMessage(), "Date range must be provided.");
  }

  @Test
  public void shouldReturnErrorResultIfDateRangeIsNotValid() {
    Optional<List<MetricType>> metricTypes = Optional.ofNullable(null);

    Optional<StatisticType> statistic = Optional.ofNullable(null);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    // end date is before from date
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 20, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    Result<SensorMetricQuery> validateResult = query.validate();

    assertEquals(validateResult.isNotOk(), true);
    assertEquals(validateResult.getExceptionMessage(),

        "Date range is invalid. Make sure the from date is less than the end date");
  }

  @Test
  public void shouldReturnAnErrorResultIfDateRangeIsGreaterThanAMonth() {
    Optional<List<MetricType>> metricTypes = Optional.ofNullable(null);

    Optional<StatisticType> statistic = Optional.ofNullable(null);

    Optional<List<Long>> sensorIds = Optional.ofNullable(null);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    // range is larger than 1 month
    LocalDateTime endDate = LocalDateTime.of(2023, 5, 20, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistic, optionalFromDate,
        optionalEndDate);

    Result<SensorMetricQuery> validateResult = query.validate();

    assertEquals(validateResult.isNotOk(), true);
    assertEquals(validateResult.getExceptionMessage(),
        "Date range must not be greater than a month");
  }
}

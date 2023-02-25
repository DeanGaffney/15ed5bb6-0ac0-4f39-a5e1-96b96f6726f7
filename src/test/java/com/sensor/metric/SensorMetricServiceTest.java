package com.sensor.metric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sensor.result.Result;
import com.sensor.statistic.StatisticType;

@ExtendWith(MockitoExtension.class)
public class SensorMetricServiceTest {
  @Mock
  private SensorMetricCrudRepository crudRepository;

  @Mock
  private SensorMetricRepository sensorMetricRepository;

  @InjectMocks
  private SensorMetricService sensorMetricService;

  @Test
  public void shouldCreateSensorMetric() {
    Long sensorId = 1l;
    List<Metric> metrics = new ArrayList<>();

    metrics.add(new Metric(MetricType.TEMPERATURE, 2.5));
    metrics.add(new Metric(MetricType.HUMIDITY, 5));

    LocalDateTime createdDate = LocalDateTime.of(2023, 2, 25, 0, 0);

    List<SensorMetric> sensorMetrics = new ArrayList<>();
    metrics.forEach(m -> sensorMetrics.add(new SensorMetric(sensorId, m, createdDate)));

    doReturn(sensorMetrics)
        .when(this.crudRepository)
        .saveAll(ArgumentMatchers.<List<SensorMetric>>any());

    Result<List<SensorMetric>> persistResult = this.sensorMetricService.createSensorMetrics(sensorId, metrics,
        createdDate);

    assertEquals(persistResult.isOk(), true);

    List<SensorMetric> persistedSensorMetrics = persistResult.getResult();
    for (int i = 0; i < sensorMetrics.size(); i += 1) {
      SensorMetric persistedMetric = persistedSensorMetrics.get(i);
      SensorMetric expectedMetric = sensorMetrics.get(i);

      assertEquals(persistedMetric.getSensorId(), expectedMetric.getSensorId());
      assertEquals(persistedMetric.getCreatedDate(), expectedMetric.getCreatedDate());
      assertEquals(persistedMetric.getMetric(), expectedMetric.getMetric());
    }
  }

  @Test
  public void shouldReturnAnErrorResultIfAFailureOccurrsSavingMetrics() {
    Long sensorId = 1l;
    List<Metric> metrics = new ArrayList<>();

    metrics.add(new Metric(MetricType.TEMPERATURE, 2.5));
    metrics.add(new Metric(MetricType.HUMIDITY, 5));

    LocalDateTime createdDate = LocalDateTime.of(2023, 2, 25, 0, 0);

    List<SensorMetric> sensorMetrics = new ArrayList<>();
    metrics.forEach(m -> sensorMetrics.add(new SensorMetric(sensorId, m, createdDate)));

    doThrow(new RuntimeException())
        .when(this.crudRepository)
        .saveAll(ArgumentMatchers.<List<SensorMetric>>any());

    Result<List<SensorMetric>> persistResult = this.sensorMetricService.createSensorMetrics(sensorId, metrics,
        createdDate);

    assertEquals(persistResult.isNotOk(), true);
    assertEquals(persistResult.getExceptionMessage(),
        "Failed to create sensor metric(s) for sensor with id " + sensorId);
  }

  @Test
  public void shouldQueryMetricsAndGroupBySensorId() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistics = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistics, optionalFromDate,
        optionalEndDate);

    List<SensorMetricQueryResult> sensorMetricQueryResults = new ArrayList<>();

    sensorMetricQueryResults
        .add(new SensorMetricQueryResult(sensorIds.get().get(0), MetricType.TEMPERATURE, new BigDecimal(2.5)));
    sensorMetricQueryResults
        .add(new SensorMetricQueryResult(sensorIds.get().get(0), MetricType.WIND_SPEED, new BigDecimal(0.5)));
    sensorMetricQueryResults
        .add(new SensorMetricQueryResult(sensorIds.get().get(1), MetricType.HUMIDITY, new BigDecimal(10)));

    Result<List<SensorMetricQueryResult>> mockedResult = Result.ok(sensorMetricQueryResults);

    doReturn(mockedResult)
        .when(this.sensorMetricRepository)
        .querySensorMetrics(query);

    Result<Map<Long, List<SensorMetricQueryResult>>> result = this.sensorMetricService.queryMetrics(query);

    assertEquals(result.isOk(), true);

    Map<Long, List<SensorMetricQueryResult>> resultsGroupBySensorId = result.getResult();

    // 2 sensor ids were requested, should contain 2 groups of metrics
    assertEquals(resultsGroupBySensorId.size(), 2);

    // sensor id 1 has two metrics
    assertEquals(resultsGroupBySensorId.get(sensorIds.get().get(0)).size(), 2);
    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(0)).get(0).getMetricType(),
        MetricType.TEMPERATURE);
    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(0)).get(0).getStatisticValue(),
        new BigDecimal(2.5));
    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(0)).get(1).getMetricType(),
        MetricType.WIND_SPEED);
    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(0)).get(1).getStatisticValue(),
        new BigDecimal(0.5));

    // sensor id 2 has one metric
    assertEquals(resultsGroupBySensorId.get(sensorIds.get().get(1)).size(), 1);

    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(1)).get(0).getMetricType(),
        MetricType.HUMIDITY);
    assertEquals(
        resultsGroupBySensorId.get(sensorIds.get().get(1)).get(0).getStatisticValue(),
        new BigDecimal(10));
  }

  @Test
  public void shouldQueryMetricsAndHandleErrorResult() {
    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistics = Optional.of(StatisticType.AVG);

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistics, optionalFromDate,
        optionalEndDate);

    Result<List<SensorMetricQueryResult>> queryResult = Result.error(new RuntimeException("Failed to query results"));

    doReturn(queryResult)
        .when(this.sensorMetricRepository)
        .querySensorMetrics(query);

    Result<Map<Long, List<SensorMetricQueryResult>>> result = this.sensorMetricService.queryMetrics(query);

    assertEquals(result.isNotOk(), true);
    assertEquals(result.getExceptionMessage(), "Failed to query metrics");
  }
}

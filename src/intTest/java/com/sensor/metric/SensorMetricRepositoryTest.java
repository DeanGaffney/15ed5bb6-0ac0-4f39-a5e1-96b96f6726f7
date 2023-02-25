package com.sensor.metric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sensor.result.Result;
import com.sensor.statistic.StatisticType;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class SensorMetricRepositoryTest {

  @Autowired
  private SensorMetricCrudRepository crudRepository;

  @Autowired
  private SensorMetricRepository sensorMetricRepository;

  @Test
  public void shouldQuerySensorMetric() {
    LocalDateTime createdDate = LocalDateTime.of(2023, 2, 25, 0, 0);

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    Metric metric1 = new Metric(MetricType.HUMIDITY, new BigDecimal(5.00));
    Metric metric2 = new Metric(MetricType.TEMPERATURE, new BigDecimal(30));
    Metric metric3 = new Metric(MetricType.WIND_SPEED, new BigDecimal(40));

    SensorMetric sensorMetric1 = new SensorMetric(sensorIds.get().get(0), metric1, createdDate);

    SensorMetric sensorMetric2 = new SensorMetric(sensorIds.get().get(0), metric2, createdDate);

    SensorMetric sensorMetric3 = new SensorMetric(sensorIds.get().get(1), metric3, createdDate);

    crudRepository.saveAll(Arrays.asList(sensorMetric1, sensorMetric2, sensorMetric3));

    Optional<List<MetricType>> metricTypes = Optional.of(
        Arrays.asList(new MetricType[] { MetricType.TEMPERATURE, MetricType.HUMIDITY }));

    Optional<StatisticType> statistics = Optional.of(StatisticType.AVG);

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 3, 23, 22, 50, 0);

    Optional<LocalDateTime> optionalFromDate = Optional.of(fromDate);
    Optional<LocalDateTime> optionalEndDate = Optional.of(endDate);

    SensorMetricQuery query = new SensorMetricQuery(metricTypes, sensorIds, statistics, optionalFromDate,
        optionalEndDate);

    Result<List<SensorMetricQueryResult>> queryResult = this.sensorMetricRepository.querySensorMetrics(query);

    assertEquals(queryResult.isOk(), true);

    List<SensorMetricQueryResult> sensorMetricResults = queryResult.getResult();

    // 3 metrics are persisted but the query does not include WIND_SPEED, so 2
    // results should be returned
    assertEquals(2, sensorMetricResults.size());

    assertEquals(sensorMetricResults.get(0).getSensorId(), sensorIds.get().get(0));
    assertEquals(sensorMetricResults.get(0).getMetricType(), metric1.getMetricType());
    assertEquals(sensorMetricResults.get(0).getStatisticValue(), metric1.getValue());

    assertEquals(sensorMetricResults.get(1).getSensorId(), sensorIds.get().get(0));
    assertEquals(sensorMetricResults.get(1).getMetricType(), metric2.getMetricType());
    assertEquals(sensorMetricResults.get(1).getStatisticValue(), metric2.getValue());
  }
}

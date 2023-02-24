package com.sensor.metric;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sensor.result.Result;

@Service
public class SensorMetricService {
  private final SensorMetricRepository sensorMetricRepository;
  private final SensorMetricCrudRepository crudRepository;

  public SensorMetricService(SensorMetricRepository sensorMetricRepository, SensorMetricCrudRepository crudRepository) {
    this.sensorMetricRepository = sensorMetricRepository;
    this.crudRepository = crudRepository;
  }

  public Result<List<SensorMetric>> createSensorMetric(Long sensorId, List<Metric> metrics, LocalDateTime currentDate) {

    try {
      List<SensorMetric> sensorMetrics = metrics.stream()
          .map(metric -> {
            return new SensorMetric(sensorId, metric, currentDate);
          })
          .collect(Collectors.toList());
      return Result.ok(this.crudRepository.saveAll(sensorMetrics));
    } catch (Exception e) {
      // TODO add logging for original exception
      return Result.error(
          new RuntimeException("Failed to create sensor metric for sensor with id " + sensorId));
    }
  }

  public Result<Map<Long, List<SensorMetricQueryResult>>> queryMetrics(SensorMetricQuery query) {
    Result<List<SensorMetricQueryResult>> queryResult = this.sensorMetricRepository.querySensorMetrics(query);

    if (queryResult.isNotOk()) {
      return Result.error(new RuntimeException("Failed to query metrics"));
    }

    System.out.println("Before mapping");
    queryResult.getResult()
        .stream()
        .peek(r -> {
          System.out.println(r.toString());
        });

    Map<Long, List<SensorMetricQueryResult>> resultsGroupedBySensorId = queryResult.getResult()
        .stream()
        .collect(Collectors.groupingBy(SensorMetricQueryResult::getSensorId));

    resultsGroupedBySensorId.keySet()
        .stream()
        .peek(id -> {
          System.out.println("SENSOR ID " + id);
          resultsGroupedBySensorId.get(id)
              .stream()
              .peek(r -> {
                System.out.println(r.toString());
              });
        });

    return Result.ok(resultsGroupedBySensorId);
  }

}

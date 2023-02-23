package com.sensor.metric;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sensor.Sensor;
import com.sensor.result.Result;

@Service
public class SensorMetricService {
  private final SensorMetricRepository sensorMetricRepository;
  private final SensorMetricCrudRepository crudRepository;

  public SensorMetricService(SensorMetricRepository sensorMetricRepository, SensorMetricCrudRepository crudRepository) {
    this.sensorMetricRepository = sensorMetricRepository;
    this.crudRepository = crudRepository;
  }

  public Result<List<SensorMetric>> createSensorMetric(Sensor sensor, List<Metric> metrics, LocalDateTime currentDate) {

    try {
      List<SensorMetric> sensorMetrics = metrics.stream()
          .map(metric -> {
            return new SensorMetric(sensor.getId(), metric, currentDate);
          })
          .collect(Collectors.toList());
      return Result.ok(this.crudRepository.saveAll(sensorMetrics));
    } catch (Exception e) {
      // TODO add logging for original exception
      return Result.error(
          new RuntimeException("Failed to create sensor metric for sensor with id " + sensor.getId()));
    }
  }

  public Result<List<SensorMetricQueryResult>> queryMetrics(SensorMetricQuery query) {
    return this.sensorMetricRepository.querySensorMetrics(query);
  }

}

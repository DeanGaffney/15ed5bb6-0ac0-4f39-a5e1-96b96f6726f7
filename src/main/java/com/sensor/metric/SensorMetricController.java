package com.sensor.metric;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class SensorMetricController {
  private final SensorMetricRepository repository;


  public SensorMetricController(SensorMetricRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/sensor/{sensorId}/metric")
  public List<SensorMetric> createSensorMetric(@RequestBody List<Metric> metrics, @PathVariable Long sensorId) {

    List<SensorMetric> sensorMetrics = metrics.stream()
      .map(metric ->  {
        return new SensorMetric(sensorId, metric);
      })
      .collect(Collectors.toList());

      return this.repository.saveAll(sensorMetrics);
  }

}

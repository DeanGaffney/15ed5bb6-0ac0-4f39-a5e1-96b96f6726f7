package com.sensor.metric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sensor.Sensor;
import com.sensor.SensorService;
import com.sensor.result.Result;

@RestController
public class SensorMetricController {
  private final SensorService sensorService;
  private final SensorMetricService sensorMetricService;

  public SensorMetricController(SensorService sensorService, SensorMetricService sensorMetricService) {
    this.sensorService = sensorService;
    this.sensorMetricService = sensorMetricService;
  }

  @PostMapping("/sensor/{sensorId}/metric")
  public ResponseEntity<?> createSensorMetric(@RequestBody List<Metric> metrics, @PathVariable Long sensorId) {
    Optional<Sensor> sensor = this.sensorService.getSensorById(sensorId);

    if (sensor.isEmpty()) {
      return new ResponseEntity<>("Sensor " + sensorId + " does not exist",
          HttpStatus.NOT_FOUND);
    }

    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);

    Result<List<SensorMetric>> persistResult = this.sensorMetricService.createSensorMetric(sensor.get(), metrics,
        currentDate);

    if (persistResult.isNotOk()) {
      return ResponseEntity.internalServerError().body("Failed to create sensor metric");
    }

    return ResponseEntity.ok(persistResult.getResult());
  }

  @PostMapping("/sensor/metric/query")
  public ResponseEntity<?> getSensorMetrics(@RequestBody SensorMetricQuery query) {

    Result<SensorMetricQuery> validateResult = query.validate();

    if (validateResult.isNotOk()) {
      return new ResponseEntity<>(validateResult.getExceptionMessage(), HttpStatus.BAD_REQUEST);
    }

    this.sensorMetricService.queryMetrics(query);
    return ResponseEntity.ok().build();

  }

}

package com.sensor.metric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sensor.api.response.ApiResponseBody;
import com.sensor.result.Result;

@RestController
public class SensorMetricController {
  private final SensorMetricService sensorMetricService;

  public SensorMetricController(SensorMetricService sensorMetricService) {
    this.sensorMetricService = sensorMetricService;
  }

  @PostMapping("/sensor/{sensorId}/metric")
  public ResponseEntity<?> createSensorMetric(@RequestBody List<Metric> metrics, @PathVariable Long sensorId) {
    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);

    Result<List<SensorMetric>> persistResult = this.sensorMetricService.createSensorMetric(sensorId, metrics,
        currentDate);

    if (persistResult.isNotOk()) {
      return ResponseEntity.internalServerError().body("Failed to create sensor metric");
    }

    return ResponseEntity.ok(persistResult.getResult());
  }

  @PostMapping("/sensor/metric/query")
  public ResponseEntity<Map<String, Object>> getSensorMetrics(@RequestBody SensorMetricQuery query) {

    Result<SensorMetricQuery> validateResult = query.validate();

    if (validateResult.isNotOk()) {
      ApiResponseBody body = ApiResponseBody.createErrorResponse(validateResult.getExceptionMessage());
      return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.BAD_REQUEST);
    }

    Result<Map<Long, List<SensorMetricQueryResult>>> queryResult = this.sensorMetricService.queryMetrics(query);

    if (queryResult.isNotOk()) {
      ApiResponseBody body = ApiResponseBody.createErrorResponse(queryResult.getExceptionMessage());
      return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // TODO make a dedicated serializer for this class
    ApiResponseBody body = new ApiResponseBody();

    Map<Long, List<SensorMetricQueryResult>> groupedResults = queryResult.getResult();

    List<Map<String, Object>> results = groupedResults.keySet()
        .stream()
        .map(sensorId -> {
          Map<String, Object> sensorResult = new HashMap<String, Object>();

          sensorResult.put("sensorId", sensorId);

          List<Map<String, Object>> metricMaps = groupedResults.get(sensorId).stream()
              .map(metricResult -> metricResult.toMap())
              .collect(Collectors.toList());

          sensorResult.put("metrics", metricMaps);
          return sensorResult;
        })
        .collect(Collectors.toList());

    body.add("statistic", query.getStatistic());
    body.add("results", results);

    return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.OK);
  }

}

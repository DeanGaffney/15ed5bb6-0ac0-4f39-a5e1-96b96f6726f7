package com.sensor.metric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  Logger logger = LoggerFactory.getLogger(SensorMetricController.class);

  @Autowired
  private final SensorMetricService sensorMetricService;

  public SensorMetricController(SensorMetricService sensorMetricService) {
    this.sensorMetricService = sensorMetricService;
  }

  @PostMapping("/sensor/{sensorId}/metric")
  public ResponseEntity<Map<String, Object>> createSensorMetrics(@RequestBody List<Metric> metrics,
      @PathVariable Long sensorId) {
    LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);

    logger.info("Creating " + metrics.size() + " metric(s) for sensor with id " + sensorId);
    Result<List<SensorMetric>> persistResult = this.sensorMetricService.createSensorMetrics(sensorId, metrics,
        currentDate);

    if (persistResult.isNotOk()) {
      ApiResponseBody body = ApiResponseBody.createErrorResponse("Failed to create sensor metric");
      return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    logger.info("Successfully created " + metrics.size() + " metric(s) for sensor id " + sensorId);
    ApiResponseBody body = new ApiResponseBody();
    body.add("result", persistResult.getResult());
    return new ResponseEntity<Map<String,Object>>(body.getBody(), HttpStatus.OK);
  }

  @PostMapping("/sensor/metric/query")
  public ResponseEntity<Map<String, Object>> getSensorMetrics(@RequestBody SensorMetricQuery query) {
    Result<SensorMetricQuery> validateResult = query.validate();

    if (validateResult.isNotOk()) {
      logger.error("Query was invalid, with reason: " + validateResult.getExceptionMessage());
      ApiResponseBody body = ApiResponseBody.createErrorResponse(validateResult.getExceptionMessage());
      return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.BAD_REQUEST);
    }

    logger.info("Executing query");

    Result<Map<Long, List<SensorMetricQueryResult>>> queryResult = this.sensorMetricService.queryMetrics(query);

    if (queryResult.isNotOk()) {
      logger.error("Failed to execute query with reason: " + queryResult.getExceptionMessage());
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

    logger.info("Successfully executed query");

    return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.OK);
  }

}

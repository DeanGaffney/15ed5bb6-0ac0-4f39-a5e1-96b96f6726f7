package com.sensor.metric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sensor.result.Result;
import com.sensor.sql.SQLQueryBuilder;

@Repository
public class SensorMetricRepository {

  Logger logger = LoggerFactory.getLogger(SensorMetricRepository.class);

  @Autowired
  private final EntityManager entityManager;

  public SensorMetricRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Result<List<SensorMetricQueryResult>> querySensorMetrics(SensorMetricQuery query) {
    SQLQueryBuilder builder = new SQLQueryBuilder();

    try {
      Map<String, Object> queryParameters = new HashMap<String, Object>();

      queryParameters.put("fromDate", query.getFromDate().get());
      queryParameters.put("endDate", query.getEndDate().get());
      queryParameters.put("metricTypes", query.getMetricsTypes());

      builder.select(Arrays.asList("sensor_id", "metric_type,"))
          .withAggregation("metric_value", query.getStatistic())
          .from("sensor_metric")
          .where()
          .dateBetween("created_date")
          .and();

      if (query.containsSensorIds()) {
        builder.whereValueIn("sensor_id", "sensorIds")
            .and();
        queryParameters.put("sensorIds", query.getSensorIds());
      }

      builder.whereValueIn("metric_type", "metricTypes");

      String querytoExecute = builder
          .groupBy(Arrays.asList("sensor_id", "metric_type"))
          .build();

      logger.info("Execuring query - " + querytoExecute);

      Query sqlQuery = entityManager.createNativeQuery(querytoExecute);

      queryParameters.keySet()
          .stream()
          .forEach(paramKey -> {
            sqlQuery.setParameter(paramKey, queryParameters.get(paramKey));
          });

      // warning is unavoidable when running a native query and casting to a POJO
      @SuppressWarnings("unchecked")
      List<Object[]> records = sqlQuery.getResultList();

      List<SensorMetricQueryResult> result = records.stream()
          .map(record -> {
            BigInteger sensorId = (BigInteger) record[0];
            MetricType metricType = MetricType.typeToValue((String) record[1]);
            BigDecimal value = (BigDecimal) record[2];
            return new SensorMetricQueryResult(sensorId.longValue(), metricType, value);
          })
          .collect(Collectors.toList());

      return Result.ok(result);
    } catch (Exception e) {
      logger.error("Failed to execute query", e);
      return Result.error(new RuntimeException("Failed to execute query"));
    }
  }
}

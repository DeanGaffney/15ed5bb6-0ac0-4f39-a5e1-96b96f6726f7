package com.sensor.metric;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sensor.statistic.StatisticType;

public class SensorMetricQueryBuilder {
  private ArrayList<String> queryParts;

  public SensorMetricQueryBuilder() {
    this.queryParts = new ArrayList<String>();
  }

  SensorMetricQueryBuilder selectWithStatistic(String columnName, StatisticType statistic) {

    String statType = statistic.getType();
    String selectPart = statType.toUpperCase() + "(" + columnName + ") as statistic_value";

    this.queryParts.add(String.join(", ", selectPart));

    return this;
  }

  SensorMetricQueryBuilder metricsMatch(String columnName, List<MetricType> metricTypes) {
    List<String> normalizedTypes = metricTypes
        .stream()
        .map(metricType -> "'" + metricType.getType() + "'")
        .collect(Collectors.toList());

    this.queryParts.add(columnName + " IN (" + String.join(", ", normalizedTypes) + ")");

    return this;
  }

  SensorMetricQueryBuilder dateBetween(String dateColumn, LocalDateTime fromDate, LocalDateTime endDate) {
    this.queryParts.add(dateColumn + " BETWEEN '" + fromDate + "' AND '" + endDate + "'");
    return this;
  }

  SensorMetricQueryBuilder whereValueIn(String columnName, List<Long> sensorIds) {
    List<String> mappedIds = sensorIds.stream().map(id -> id.toString()).collect(Collectors.toList());
    this.queryParts.add(columnName + " IN (" + String.join(", ", mappedIds) + ")");
    return this;

  }

  SensorMetricQueryBuilder groupBy(List<String> columnNames) {
    String formattedColumnNames = String.join(", ", columnNames);
    this.queryParts.add("GROUP BY " + formattedColumnNames);
    return this;
  }

  SensorMetricQueryBuilder from(String tableName) {
    this.queryParts.add("FROM " + tableName);
    return this;
  }

  SensorMetricQueryBuilder and() {
    this.queryParts.add("AND");
    return this;
  }

  SensorMetricQueryBuilder where() {
    this.queryParts.add("WHERE");
    return this;
  }

  SensorMetricQueryBuilder select(List<String> columnNames) {
    this.queryParts.add(String.join(", ", columnNames));
    return this;
  }

  public String build() {
    return "SELECT " + String.join(" ", this.queryParts);
  }

}

package com.sensor.sql;

import java.util.ArrayList;
import java.util.List;

import com.sensor.statistic.StatisticType;

public class SQLQueryBuilder {
  private ArrayList<String> queryParts;

  public SQLQueryBuilder() {
    this.queryParts = new ArrayList<String>();
  }

  public SQLQueryBuilder withAggregation(String columnName, StatisticType statistic) {
    String statType = statistic.getType();
    String selectPart = statType.toUpperCase() + "(" + columnName + ") as statistic_value";

    this.queryParts.add(String.join(", ", selectPart));

    return this;
  }

  public SQLQueryBuilder dateBetween(String dateColumn) {
    this.queryParts.add(dateColumn + " BETWEEN :fromDate AND :endDate");
    return this;
  }

  public SQLQueryBuilder whereValueIn(String columnName, String parameterName) {
    this.queryParts.add(columnName + " IN :" + parameterName);
    return this;
  }

  public SQLQueryBuilder groupBy(List<String> columnNames) {
    String formattedColumnNames = String.join(", ", columnNames);
    this.queryParts.add("GROUP BY " + formattedColumnNames);
    return this;
  }

  public SQLQueryBuilder from(String tableName) {
    this.queryParts.add("FROM " + tableName);
    return this;
  }

  public SQLQueryBuilder and() {
    this.queryParts.add("AND");
    return this;
  }

  public SQLQueryBuilder where() {
    this.queryParts.add("WHERE");
    return this;
  }

  public SQLQueryBuilder select(List<String> columnNames) {
    this.queryParts.add(String.join(", ", columnNames));
    return this;
  }

  public String build() {
    return "SELECT " + String.join(" ", this.queryParts);
  }

}

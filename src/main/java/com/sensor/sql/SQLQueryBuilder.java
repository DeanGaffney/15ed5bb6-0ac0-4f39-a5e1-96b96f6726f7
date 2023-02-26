package com.sensor.sql;

import java.util.ArrayList;
import java.util.List;

import com.sensor.statistic.StatisticType;

/**
 * Class for building SQL queries for sensor metric
 * related queries.
 *
 */
public class SQLQueryBuilder {
  private ArrayList<String> queryParts;

  public SQLQueryBuilder() {
    this.queryParts = new ArrayList<String>();
  }

  /**
   * Adds an aggregation using the given {@link StatisticType} to the sql query
   *
   * @param columnName the name of the column to aggregate
   * @param statistic the type of statistic to use for the aggregation
   * @return this instance
   */
  public SQLQueryBuilder withAggregation(String columnName, StatisticType statistic) {
    String statType = statistic.getType();
    String selectPart = statType.toUpperCase() + "(" + columnName + ") as statistic_value";

    this.queryParts.add(String.join(", ", selectPart));

    return this;
  }

  /**
   * Adds a between clause for date ranges to the sql query
   *
   * @param dateColumn the name of the date column
   * @return this instance
   */
  public SQLQueryBuilder dateBetween(String dateColumn) {
    this.queryParts.add(dateColumn + " BETWEEN :fromDate AND :endDate");
    return this;
  }

  /**
   * Adds an IN clause to the sql query for the given column name
   *
   * @param columnName the name of the column to use for the IN clause
   * @param parameterName the name of the parameter to use for the prepared statement
   * @return this instance
   */
  public SQLQueryBuilder whereValueIn(String columnName, String parameterName) {
    this.queryParts.add(columnName + " IN :" + parameterName);
    return this;
  }

  /**
   * Adds a group by clause to the dsql query
   *
   * @param columnNames the list of column names to include in the group clause
   * @return this instance
   */
  public SQLQueryBuilder groupBy(List<String> columnNames) {
    String formattedColumnNames = String.join(", ", columnNames);
    this.queryParts.add("GROUP BY " + formattedColumnNames);
    return this;
  }

  /**
   * Adds a from clause to the sql query
   *
   * @param tableName the table name for the from clause
   * @return this instance
   */
  public SQLQueryBuilder from(String tableName) {
    this.queryParts.add("FROM " + tableName);
    return this;
  }

  /**
   * Adds an AND to the sql query
   *
   * @return this instance
   */
  public SQLQueryBuilder and() {
    this.queryParts.add("AND");
    return this;
  }

  /**
   * Adds a WHERE to the sql query
   *
   * @return 
   */
  public SQLQueryBuilder where() {
    this.queryParts.add("WHERE");
    return this;
  }

  /**
   * Adds a select statement to the sql query
   *
   * @param columnNames the column names to include in the select
   * @return 
   */
  public SQLQueryBuilder select(List<String> columnNames) {
    this.queryParts.add(String.join(", ", columnNames));
    return this;
  }

  /**
   * Constructs the full sql query
   *
   * @return 
   */
  public String build() {
    return "SELECT " + String.join(" ", this.queryParts);
  }

}

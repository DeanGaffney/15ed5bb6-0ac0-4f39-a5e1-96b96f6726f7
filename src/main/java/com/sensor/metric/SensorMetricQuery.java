package com.sensor.metric;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sensor.result.Result;
import com.sensor.statistic.StatisticType;

public class SensorMetricQuery {
  private Optional<List<MetricType>> metrics;

  private Optional<List<Long>> sensorIds;

  private Optional<StatisticType> statistic;

  private Optional<LocalDateTime> fromDate;

  private Optional<LocalDateTime> endDate;

  public SensorMetricQuery(
      Optional<List<MetricType>> metrics,
      Optional<List<Long>> sensorIds,
      Optional<StatisticType> statistic,
      Optional<LocalDateTime> fromDate,
      Optional<LocalDateTime> endDate) {
    this.metrics = metrics;
    this.sensorIds = sensorIds;
    this.statistic = statistic;
    this.fromDate = fromDate;
    this.endDate = endDate;
  }

  public boolean containsSensorIds() {
    return this.sensorIds.isPresent() && this.sensorIds.get().size() > 0;
  }

  private boolean containsDateRange() {
    return this.fromDate.isPresent() && this.endDate.isPresent();
  }

  public boolean containsValidDateRange() {
    return this.fromDate.get().isBefore(this.endDate.get());
  }

  public List<MetricType> getMetrics() {
    // default to returning all metrics if none are specified
    return this.metrics.orElse(Arrays.asList(MetricType.values()));
  }

  public List<String> getMetricsTypes() {
    return this.getMetrics()
        .stream()
        .map(m -> m.getType())
        .collect(Collectors.toList());
  }

  public StatisticType getStatistic() {
    return this.statistic.orElse(StatisticType.AVG);
  }

  public List<Long> getSensorIds() {
    return this.sensorIds.orElse(Collections.emptyList());
  }

  public Optional<LocalDateTime> getFromDate() {
    return this.fromDate;
  }

  public Optional<LocalDateTime> getEndDate() {
    return this.endDate;
  }

  public Result<SensorMetricQuery> validate() {
    boolean containsValidMetricTypes = this.getMetrics().stream().allMatch(metricType -> {
      return Arrays.asList(MetricType.values()).contains(metricType);
    });

    if (!containsValidMetricTypes) {
      return Result.error(new InvalidParameterException("Query contains invalid metric types"));
    }

    boolean containsValidStatisitics = Arrays.asList(StatisticType.values()).contains(this.getStatistic());

    if (!containsValidStatisitics) {
      return Result.error(new InvalidParameterException("Query contains invalid statistic type"));
    }

    if (!this.containsDateRange()) {
      return Result.error(new InvalidParameterException("Date range must be provided."));
    }

    if (!this.containsValidDateRange()) {
      return Result.error(
          new InvalidParameterException("Date range is invalid. Make sure the from date is less than the end date"));
    }

    long difference = ChronoUnit.MONTHS.between(this.fromDate.get(), this.endDate.get());
    if (difference >= 1) {
      return Result.error(new InvalidParameterException("Date range must not be greater than a month"));
    }

    return Result.ok(this);
  }
}

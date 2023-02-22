package com.sensor.metric;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.jmx.support.MetricType;

import com.sensor.result.Result;

public class SensorMetricQuery {
  private List<MetricType> metricTypes;

  private Optional<List<Long>> sensorIds;

  private Optional<LocalDateTime> fromDate;

  private Optional<LocalDateTime> endDate;

  public SensorMetricQuery(
      List<MetricType> metricTypes,
      Optional<List<Long>> sensorIds,
      Optional<LocalDateTime> fromDate,
      Optional<LocalDateTime> endDate) {
    this.metricTypes = metricTypes;
    this.sensorIds = sensorIds;
    this.fromDate = fromDate;
    this.endDate = endDate;
  }

  public boolean containsSensorIds() {
    return this.sensorIds.isPresent() && this.sensorIds.get().size() > 0;
  }

  public boolean containsDateRange() {
    return this.fromDate.isPresent() && this.endDate.isPresent();
  }

  public boolean containsValidDateRange() {
    return this.fromDate.get().compareTo(this.endDate.get()) >= 0;
  }

  public List<MetricType> getMetricTypes() {
    return this.metricTypes;
  }

  public Result<SensorMetricQuery> validate() {
    boolean containsValidMetricTypes = this.metricTypes.stream().allMatch(metricType -> {
      return Arrays.asList(MetricType.values()).contains(metricType);
    });

    if (!containsValidMetricTypes) {
      return Result.error(new InvalidParameterException("Query contains invalid metric types"));
    }

    // TODO refactor all date logic and functions in here out to a DateRange object
    if (this.containsDateRange() && !this.containsValidDateRange()) {
      return Result.error(
          new InvalidParameterException("Date Range is invalid. Make sure the from date is less than the end date"));
    }

    long difference = ChronoUnit.MONTHS.between(this.fromDate.get(), this.endDate.get());
    if (difference >= 1) {
      return Result.error(new InvalidParameterException("Date range must not be greater than a month"));
    }

    return Result.ok(this);
  }
}

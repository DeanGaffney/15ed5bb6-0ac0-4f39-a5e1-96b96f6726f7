package com.sensor.metric;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
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
      Optional<LocalDateTime> endDate
    ) {
    this.metricTypes = metricTypes;
    this.sensorIds = sensorIds;
    this.fromDate = fromDate;
    this.endDate = endDate;
  }

  public boolean containsSensorIds() {
    return this.sensorIds.isPresent() && this.sensorIds.get().size() > 0;
  }

  public boolean containsDateRange(){
    return this.fromDate.isPresent() && this.endDate.isPresent();
  }

  public List<MetricType> getMetricTypes() {
    return this.metricTypes;
  }

  public Result<SensorMetricQuery, InvalidParameterException> validate(){
    // this.metricTypes.stream().allMatch(metricType -> 
    // check if metric types are valid
    // check if from date is less than end date if present
    // check if date range is less than or equal to a month
    //
    // return the Result for the controller or service to handle
  }
}

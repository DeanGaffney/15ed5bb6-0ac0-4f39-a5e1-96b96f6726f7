package com.sensor.metric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorMetricRepository extends JpaRepository<SensorMetric, Long> {


}

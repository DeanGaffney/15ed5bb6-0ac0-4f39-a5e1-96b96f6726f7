package com.sensor.metric;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sensor.Sensor;
import com.sensor.SensorRepository;

@Service
public class SensorService {
  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  public Optional<Sensor> getSensorById(Long sensorId) {
    return this.sensorRepository.findById(sensorId);
  }
}

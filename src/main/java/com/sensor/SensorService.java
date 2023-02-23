package com.sensor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  public Optional<Sensor> getSensorById(Long sensorId) {
    return this.sensorRepository.findById(sensorId);
  }

  public Sensor createSensor(Sensor sensor) {
    return this.sensorRepository.save(sensor);
  }

  public List<Sensor> findAllSensors() {
    return this.sensorRepository.findAll();
  }
}

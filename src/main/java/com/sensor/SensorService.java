package com.sensor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service("sensorService")
public class SensorService {

  private final SensorRepository sensorRepository;

  public SensorService(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  public Sensor createSensor(Sensor sensor) {
    return this.sensorRepository.save(sensor);
  }

  public List<Sensor> findAllSensors() {
    return this.sensorRepository.findAll();
  }
}

package com.sensor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {
  private final SensorRepository repository;

  public SensorController(SensorRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/sensor")
  public Sensor createSensor(@Valid @RequestBody Sensor sensor) {
    return this.repository.save(sensor);
  }

  @GetMapping("/sensor")
  public List<Sensor> getAllSensors(){
    return this.repository.findAll();
  }
}

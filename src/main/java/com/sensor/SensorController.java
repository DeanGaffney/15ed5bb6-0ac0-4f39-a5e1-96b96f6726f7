package com.sensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {

  @Autowired
  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @PostMapping("/sensor")
  public Sensor createSensor(@Valid @RequestBody Sensor sensor) {
    return this.sensorService.createSensor(sensor);
  }

  @GetMapping("/sensor")
  public Map<String, List<Sensor>> getAllSensors() {
    List<Sensor> sensors = this.sensorService.findAllSensors();

    // TODO convert to a response object
    Map<String, List<Sensor>> body = new HashMap<String, List<Sensor>>();

    body.put("sensors", sensors);

    return body;
  }
}

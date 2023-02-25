// package com.sensor;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
//
// import org.junit.jupiter.api.Test;
//
// @SpringBootTest
// public class SensorRepositoryTest {
//   @Autowired
//   private SensorRepository sensorRepository;
//
//   @Test
//   public void shouldSaveSensor() {
//     Sensor sensor = new Sensor("test sensor");
//     Sensor persistedSensor = sensorRepository.save(sensor);
//
//     Sensor retrievedSensor = sensorRepository.findById(persistedSensor.getId()).get();
//
//     assertEquals(retrievedSensor.getId(), persistedSensor.getId());
//     assertEquals(retrievedSensor.getName(), sensor.getName());
//   }
// }

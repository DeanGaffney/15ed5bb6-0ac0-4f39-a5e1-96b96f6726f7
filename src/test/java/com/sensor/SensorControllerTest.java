package com.sensor;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SensorController.class)
public class SensorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean(name = "sensorService")
  private SensorService sensorService;

  @Test
  public void shouldSaveSensorAndReturnOk() throws Exception {
    Sensor sensor = new Sensor("test sensor");

    Sensor persistedSensor = new Sensor(sensor.getName());
    persistedSensor.setId(1l);

    when(this.sensorService.createSensor(any(Sensor.class)))
        .thenReturn(persistedSensor);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders
        .post("/sensor")
        .content(new ObjectMapper().writeValueAsString(sensor))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(persistedSensor.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(persistedSensor.getName()));
  }

  @Test
  public void shouldFindAllSensors() throws Exception {
    List<Sensor> sensors = new ArrayList<Sensor>();

    int iterations = 10;

    for (int i = 0; i < iterations; i++) {
      Sensor persistedSensor = new Sensor("test sensor " + i + 1);
      persistedSensor.setId((long) i + 1);
      sensors.add(persistedSensor);
    }

    when(this.sensorService.findAllSensors()).thenReturn(sensors);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders
        .get("/sensor")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.sensors", hasSize(iterations)));
  }
}

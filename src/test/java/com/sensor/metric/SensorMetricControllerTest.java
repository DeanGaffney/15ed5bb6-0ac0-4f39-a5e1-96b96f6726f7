package com.sensor.metric;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sensor.result.Result;

@WebMvcTest(SensorMetricController.class)
public class SensorMetricControllerTest {

  @MockBean
  private SensorMetricService sensorMetricService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void shouldCreateSensorMetrics() throws Exception {
    Long sensorId = 1l;
    LocalDateTime createdDate = LocalDateTime.of(2023, 3, 25, 0, 0);

    List<Metric> metrics = new ArrayList<>();
    metrics.add(new Metric(MetricType.WIND_SPEED, new BigDecimal(0.5)));

    List<SensorMetric> sensorMetrics = Arrays.asList(new SensorMetric(sensorId, metrics.get(0), createdDate));

    when(this.sensorMetricService.createSensorMetrics(ArgumentMatchers.eq(sensorId), ArgumentMatchers.<Metric>anyList(),
        ArgumentMatchers.any()))
        .thenReturn(Result.ok(sensorMetrics));

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders.post("/sensor/" + sensorId + "/metric")
        .content(new ObjectMapper().writeValueAsString(metrics))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result", hasSize(1)));
  }

  @Test
  public void shouldQueryMetrics() throws Exception {
    Map<Long, List<SensorMetricQueryResult>> groupedResults = new HashMap<>();

    groupedResults.put(1l, Arrays.asList(new SensorMetricQueryResult(1l, MetricType.TEMPERATURE, new BigDecimal(2.5))));
    groupedResults.put(2l, Arrays.asList(new SensorMetricQueryResult(2l, MetricType.WIND_SPEED, new BigDecimal(10))));

    when(this.sensorMetricService.queryMetrics(ArgumentMatchers.any(SensorMetricQuery.class)))
        .thenReturn(Result.ok(groupedResults));

    Optional<List<Long>> sensorIds = Optional.of(Arrays.asList(new Long[] { 1l, 2l }));

    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    ObjectMapper jsonMapper = new ObjectMapper();
    ObjectNode jsonNode = jsonMapper.createObjectNode();

    jsonNode.put("fromDate", fromDate.toString());
    jsonNode.put("endDate", endDate.toString());

    jsonNode.put("sensorIds", endDate.toString());
    jsonNode.put("statistic", "AVG");

    ArrayNode metricsJsonArr = jsonMapper.valueToTree(Arrays.asList("TEMPERATURE", "WIND_SPEED"));
    jsonNode.putArray("metrics").addAll(metricsJsonArr);

    ArrayNode sensorIdsJsonArr = jsonMapper.valueToTree(sensorIds.get());
    jsonNode.putArray("sensorIds").addAll(sensorIdsJsonArr);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders.post("/sensor/metric/query")
        .content(jsonNode.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.statistic", is("AVG")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].sensorId", is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].metrics", hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].metrics[0].metricType", is("TEMPERATURE")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].metrics[0].value", is(2.5)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].sensorId", is(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].metrics", hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].metrics[0].metricType", is("WIND_SPEED")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results[1].metrics[0].value", is(10.0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.results", hasSize(2)));
  }

  @Test
  public void shouldErrorForInvalidMetricType() throws Exception {
    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    ObjectMapper jsonMapper = new ObjectMapper();
    ObjectNode jsonNode = jsonMapper.createObjectNode();

    jsonNode.put("fromDate", fromDate.toString());
    jsonNode.put("endDate", endDate.toString());
    jsonNode.put("statistic", "AVG");

    ArrayNode metricsJsonArr = jsonMapper.valueToTree(Arrays.asList("INVALID"));
    jsonNode.putArray("metrics").addAll(metricsJsonArr);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders.post("/sensor/metric/query")
        .content(jsonNode.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error",
            is("Invalid value \"INVALID\". Value should be one of: WIND_SPEED, HUMIDITY, TEMPERATURE")));
  }

  @Test
  public void shouldErrorForInvalidStatistic() throws Exception {
    LocalDateTime fromDate = LocalDateTime.of(2023, 2, 23, 20, 50, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    ObjectMapper jsonMapper = new ObjectMapper();
    ObjectNode jsonNode = jsonMapper.createObjectNode();

    jsonNode.put("fromDate", fromDate.toString());
    jsonNode.put("endDate", endDate.toString());
    jsonNode.put("statistic", "INVALID");

    ArrayNode metricsJsonArr = jsonMapper.valueToTree(Arrays.asList("TEMPERATURE"));
    jsonNode.putArray("metrics").addAll(metricsJsonArr);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders.post("/sensor/metric/query")
        .content(jsonNode.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error",
            is("Invalid value \"INVALID\". Value should be one of: AVG, SUM, MAX, MIN")));
  }

  @Test
  public void shouldErrorForInvalidDateFormat() throws Exception {
    LocalDateTime endDate = LocalDateTime.of(2023, 2, 23, 22, 50, 0);

    ObjectMapper jsonMapper = new ObjectMapper();
    ObjectNode jsonNode = jsonMapper.createObjectNode();

    jsonNode.put("fromDate", "2023");
    jsonNode.put("endDate", endDate.toString());
    jsonNode.put("statistic", "AVG");

    ArrayNode metricsJsonArr = jsonMapper.valueToTree(Arrays.asList("TEMPERATURE"));
    jsonNode.putArray("metrics").addAll(metricsJsonArr);

    MockHttpServletRequestBuilder requestbuilder = MockMvcRequestBuilders.post("/sensor/metric/query")
        .content(jsonNode.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(requestbuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error",
            is("Invalid date provided in request. Date should be in the format: yyyy-MM-dd'T'HH:mm:ss. For example 2023-02-26T00:00")));
  }
}

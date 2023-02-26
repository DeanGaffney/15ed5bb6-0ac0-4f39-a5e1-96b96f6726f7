package com.sensor.api.response;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing an API response body
 * Allows custom responses to be returned by using
 * the result of the "getBody" call with a ResponseEntity
 *
 */
public class ApiResponseBody {
  private Map<String, Object> body;

  public ApiResponseBody() {
    this.body = new HashMap<String, Object>();
  }

  public static ApiResponseBody createErrorResponse(String errorMessage) {
    return new ApiResponseBody().add("error", errorMessage);
  }

  public ApiResponseBody add(String key, Object value) {
    this.body.put(key, value);
    return this;
  }

  public Map<String, Object> getBody() {
    return this.body;
  }

}

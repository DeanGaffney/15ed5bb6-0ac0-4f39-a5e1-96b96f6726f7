package com.sensor.api;

public class ApiResponse <T> {
  private T body;

  public ApiResponse(T body) {
    this.body = body;
  }

}

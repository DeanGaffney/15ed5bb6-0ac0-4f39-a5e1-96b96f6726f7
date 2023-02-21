package com.sensor.result;


public class Result<T, E> {
  private T result;
  private E error;

  public Result(T result, E error) {
    this.result = result;
    this.error = error;
  }

  public static <T, E> Result<T, E> ok(T result) {
    return new Result<T,E>(result, null);
  }

  public static <T, E> Result<T, E> error(E error) {
    return new Result<T,E>(null, error);
  }

  public boolean isOk() {
    return this.result != null && this.error == null;
  }

  public boolean isNotOk() {
    return !this.isOk();
  }

}

package com.sensor.result;


public class Result<T> {
  private T result;
  private Exception error;

  public Result(T result, Exception error) {
    this.result = result;
    this.error = error;
  }

  public static <T> Result<T> ok(T result) {
    return new Result<T>(result, null);
  }

  public static <T> Result<T> error(Exception error) {
    return new Result<T>(null, error);
  }

  public T getResult() {
    return this.result;
  }

  public String getExceptionMessage() {
    return this.error.getMessage();
  }

  public boolean isOk() {
    return this.result != null && this.error == null;
  }

  public boolean isNotOk() {
    return !this.isOk();
  }

}

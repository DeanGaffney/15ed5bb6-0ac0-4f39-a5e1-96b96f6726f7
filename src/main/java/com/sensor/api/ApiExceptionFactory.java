package com.sensor.api;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sensor.api.response.ApiResponseBody;

public class ApiExceptionFactory {

  // handle invalid enum exceptions
  private static final Pattern ENUM_MSG = Pattern
      .compile("from String (.*): not one of the values accepted for Enum class: \\[(.*)\\]");

  private static final Pattern DATE_MSG = Pattern
      .compile("Cannot deserialize value of type.*LocalDateTime");

  public static ResponseEntity<Map<String, Object>> createResponse(HttpMessageNotReadableException exception) {
    if (exception.getCause() != null && exception.getCause() instanceof InvalidFormatException) {
      Matcher enumMatch = ENUM_MSG.matcher(exception.getCause().getMessage());
      if (enumMatch.find()) {
        ApiResponseBody body = ApiResponseBody
            .createErrorResponse(
                "Invalid value " + enumMatch.group(1) + ". Value should be one of: " + enumMatch.group(2));
        return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.BAD_REQUEST);
      }

      Matcher dateMatch = DATE_MSG.matcher(exception.getCause().getMessage());
      if (dateMatch.find()) {
        ApiResponseBody body = ApiResponseBody
            .createErrorResponse(
                "Invalid date provided in request. "
                    + "Date should be in the format: yyyy-MM-dd'T'HH:mm:ss. For example "
                    + LocalDateTime.of(2023,2,26,0,0).toString());
        return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.BAD_REQUEST);
      }
    }

    ApiResponseBody body = ApiResponseBody.createErrorResponse(exception.getCause().getMessage());
    return new ResponseEntity<Map<String, Object>>(body.getBody(), HttpStatus.BAD_REQUEST);
  }
}

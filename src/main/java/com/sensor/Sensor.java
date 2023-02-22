package com.sensor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Sensor {
  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "Name is required")
  private String name;

  public Sensor() {
  }

  public Sensor(String name) {
    this.name = name;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }
}

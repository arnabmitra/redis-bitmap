package com.amitra.rediscount.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@AutoProperty
public class ActionEvent implements Serializable, Comparable<ActionEvent> {
  private static final long serialVersionUID = 1L;

  LocalDateTime eventTime;

  String eventType;

  Long customerId;

  public ActionEvent(LocalDateTime eventTime, String eventType, Long customerId) {
    this.eventTime = eventTime;
    this.eventType = eventType;
    this.customerId = customerId;
  }

  public LocalDateTime getEventTime() {
    return eventTime;
  }

  public String getEventType() {
    return eventType;
  }

  public Long getCustomerId() {
    return customerId;
  }

  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return Pojomatic.equals(this, obj);
  }

  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }

  @Override
  public int compareTo(ActionEvent other) {
    if (this.eventTime == null || other.eventTime == null) {
      if (this.eventTime == null && other.eventTime == null) {
        return 0;
      }

      //Nulls last
      int result = (this.eventTime == null ? 1 : -1);

      return result;
    }
    return this.eventTime.compareTo(other.eventTime);
  }

}

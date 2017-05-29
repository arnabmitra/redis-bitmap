package com.amitra.rediscount.service;


public interface CustomerService {

  void setUserAction(String userKey, long customerId);

  boolean isUserAction(String userKey,Long customerId);

  long getNumberOfUserClicksPerAction(String userKey);

  long getNumberOfUserClicksAllAction(String... userKey);
}

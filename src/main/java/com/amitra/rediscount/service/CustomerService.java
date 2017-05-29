package com.amitra.rediscount.service;


public interface CustomerService {

  /**
   * Set the user action for the customer.
   * @param userKey The key to the bitmap.
   * @param customerId the customer id for which this is being looked up for.
   */
  void setUserAction(String userKey, long customerId);

  /**
   * Has an user action been performed.
   * @param userKey The key to the bitmap.
   * @param customerId the customer id for which this is being looked up for.
   * @return Boolean true or false.
   */
  boolean isUserAction(String userKey,Long customerId);

  long getNumberOfUserClicksPerAction(String userKey);

  long getNumberOfUserClicksAllAction(String... userKey);

}

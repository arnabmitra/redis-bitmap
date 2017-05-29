package com.amitra.rediscount.redis;

public interface RedisRepository {

  void setUserAction(String userKey, long customerId);

  boolean isUserAction(String userKey, Long customerId);

  long getNumberOfUserClicksPerAction(String userKey);

  long getNumberOfUserClicksPerAction(String... userKey);

}

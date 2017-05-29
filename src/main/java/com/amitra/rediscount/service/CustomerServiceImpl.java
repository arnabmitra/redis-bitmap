package com.amitra.rediscount.service;

import com.amitra.rediscount.redis.RedisRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Inject RedisRepository redisRepository;

  @Override public void setUserAction(String userKey, long customerId) {
    redisRepository.setUserAction(userKey,customerId);
  }

  @Override public boolean isUserAction(String userKey, Long customerId) {
    return redisRepository.isUserAction(userKey,customerId);
  }

  @Override public long getNumberOfUserClicksPerAction(String userKey) {
    return redisRepository.getNumberOfUserClicksPerAction(userKey);
  }

  @Override
  public long getNumberOfUserClicksAllAction(String... userKey) {
    return redisRepository.getNumberOfUserClicksPerAction(userKey);
  }
}

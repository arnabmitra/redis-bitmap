package com.amitra.rediscount.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.BitSet;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

  private final RedisTemplate<String,Boolean> redisTemplate;

  @Inject
  public RedisRepositoryImpl(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }


  @Override
  public boolean isUserAction(String userKey,Long customerId) {
    return getBit(userKey,customerId);
  }

  @Override
  public void setUserAction(String userKey, long customerId) {
    setBit( userKey, customerId, true );
  }

  private boolean getBit( final String key, final long offset ) {
    return redisTemplate.execute(
        new RedisCallback< Boolean >() {
          @Override
          public Boolean doInRedis( RedisConnection connection ) throws DataAccessException {
            return connection.getBit( ( (RedisSerializer< String >)redisTemplate.getKeySerializer() ).serialize( key ), offset );
          }
        }
    );
  }

  @Override public long getNumberOfUserClicksPerAction(String userKey) {
    return redisTemplate.execute(new RedisCallback<Long>() {
      @Override public Long doInRedis(RedisConnection connection) throws DataAccessException {
        return connection.bitCount(( ( RedisSerializer< String > )redisTemplate.getKeySerializer() ).serialize(userKey));
      }
    });
  }

  @Override public long getNumberOfUserClicksPerAction(String... userKey) {
    BitSet  initial=getBitSet(userKey[0]);
    for (String userKeyStr:userKey)
   {
      initial.and(getBitSet(userKeyStr));
   }
   return initial.cardinality();
  }

  private BitSet getBitSet(String userKeyStr) {
    return redisTemplate.execute(new RedisCallback<BitSet>() {
      @Override public BitSet doInRedis(RedisConnection connection) throws DataAccessException {
        return BitSet.valueOf(connection.get(( (RedisSerializer< String >)redisTemplate.getKeySerializer() ).serialize(userKeyStr)));
      }
    });
  }

  private void setBit( final String key, final long offset, final boolean value ) {
    redisTemplate.execute((RedisCallback<Void>) connection -> {
      connection.setBit( ( ( RedisSerializer< String > )redisTemplate.getKeySerializer() ).serialize( key ), offset, value );
      return null;
    });
  }
}

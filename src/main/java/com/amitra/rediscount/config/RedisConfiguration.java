package com.amitra.rediscount.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import javax.inject.Provider;
import java.util.List;

@Configuration
public class RedisConfiguration {

  @Component
  @ConfigurationProperties(prefix = "spring.redis.cluster")
  public class ClusterConfigurationProperties {

    /*
     * spring.redis.cluster.nodes[0] = 127.0.0.1:7379
     * spring.redis.cluster.nodes[1] = 127.0.0.1:7380
     * ...
     */
    List<String> nodes;

    /**
     * Get initial collection of known cluster nodes in format {@code host:port}.
     *
     * @return
     */
    public List<String> getNodes() {
      return nodes;
    }

    public void setNodes(List<String> nodes) {
      this.nodes = nodes;
    }
  }

  /*
    * spring.redis.cluster.nodes[0] = 127.0.0.1:7379
    * spring.redis.cluster.nodes[1] = 127.0.0.1:7380
    * ...
    */
  List<String> nodes;

  /**
   * Type safe representation of application.properties
   */
  @Autowired
  ClusterConfigurationProperties clusterProperties;

  @Value("${redis.maxidle}")
  private Provider<Integer> maxIdleConnectionsForPool;

  @Value("${redis.maxtotal}")
  private Provider<Integer> maxTotalConnectionsForPool;

  @Value("${redis.socket.timeout}")
  private Provider<Integer> socketTimeOut;

  @Value("${spring.redis.cluster.nodes[0]}")
  private Provider<String> firstClusterHost;

  @Value("${redis.type}")
  private Provider<String> redisType;


  @Bean JedisConnectionFactory jedisConnectionFactory() {
    if ("single".equalsIgnoreCase(redisType.get())) {
      JedisConnectionFactory jcf = new JedisConnectionFactory(getShardInfo());
      return jcf;
    }
    else {
      JedisConnectionFactory jedisConnectionFactory= new JedisConnectionFactory(
          new RedisClusterConfiguration(clusterProperties.getNodes()));
      jedisConnectionFactory.setTimeout(socketTimeOut.get());
      JedisPoolConfig poolConfig=jedisConnectionFactory.getPoolConfig();
      //https://github.com/xetorthio/jedis/wiki/FAQ
      poolConfig.setMaxTotal(maxTotalConnectionsForPool.get());
      poolConfig.setMaxIdle(maxIdleConnectionsForPool.get());
      jedisConnectionFactory.setPoolConfig(poolConfig);
      return jedisConnectionFactory;
    }
  }

  @Bean StringRedisTemplate template() {
    return new StringRedisTemplate(jedisConnectionFactory());
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }


  private JedisShardInfo getShardInfo() {
    return new JedisShardInfo(firstClusterHost.get().split(":")[0],Integer.
        parseInt(firstClusterHost.get().split(":")[1]),socketTimeOut.get());
  }
}

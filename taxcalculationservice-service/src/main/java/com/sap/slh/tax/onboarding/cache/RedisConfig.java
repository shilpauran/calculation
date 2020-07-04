package com.sap.slh.tax.onboarding.cache;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.RedisURI;
import io.pivotal.cfenv.core.CfEnv;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	@Value("${redis.cache.connection.pool.max.idle:100}")
	private int redisCacheConnectionPoolMaxIdle;
	@Value("${redis.cache.connection.pool.min.idle:50}")
	private int redisCacheConnectionPoolMinIdle;
	@Value("${redis.cache.connection.pool.max.total:100}")
	private int redisCacheConnectionPoolMaxTotal;
	@Value("${redis.cache.connection.pool.max.wait.millis:-1}")
	private int redisCacheConnectionPoolMaxWaitMillis;

	@Bean
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(getJedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public JedisConnectionFactory getJedisConnectionFactory() {
		CfEnv cfEnv = new CfEnv();
		String tag = "redis";
		String redisHost = cfEnv.findCredentialsByTag(tag).getHost();
		if (redisHost != null) {
			String redisPort = cfEnv.findCredentialsByTag(tag).getPort();
			String redisPassword = cfEnv.findCredentialsByTag(tag).getPassword();
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost,
					Integer.parseInt(redisPort));
			redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
			redisStandaloneConfiguration.setDatabase(0);
			JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
					.poolConfig(getJedisPoolConfig()).build();
			JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
					jedisClientConfiguration);
			jedisConnectionFactory.afterPropertiesSet();
			return jedisConnectionFactory;
		} else {
			String uri = cfEnv.findCredentialsByTag(tag).getUri();
			RedisURI redisURI = RedisURI.create(uri);
			RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
			redisSentinelConfiguration.master(redisURI.getSentinelMasterId());
			List<RedisNode> nodes = redisURI.getSentinels().stream()
					.map(redisUri -> populateNode(redisUri.getHost(), redisUri.getPort())).collect(Collectors.toList());
			nodes.forEach(node -> redisSentinelConfiguration.addSentinel(node));
			redisSentinelConfiguration.setPassword(RedisPassword.of(redisURI.getPassword()));
			redisSentinelConfiguration.setDatabase(0);
			JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
					.poolConfig(getJedisPoolConfig()).build();
			JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration,
					jedisClientConfiguration);
			jedisConnectionFactory.afterPropertiesSet();
			return jedisConnectionFactory;
		}
	}

	private JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(redisCacheConnectionPoolMaxTotal);
		poolConfig.setMaxIdle(redisCacheConnectionPoolMaxIdle);
		poolConfig.setMinIdle(redisCacheConnectionPoolMinIdle);
		poolConfig.setMaxWaitMillis(redisCacheConnectionPoolMaxWaitMillis);
		return poolConfig;
	}

	private RedisNode populateNode(String host, Integer port) {
		return new RedisNode(host, port);
	}	
}
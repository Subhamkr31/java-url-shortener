package com.thenriquedb.url_shortener.configuration;

import io.lettuce.core.RedisCredentials;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Configuration
@EnableMongoRepositories(basePackages = "com.thenriquedb.url_shortener.repositories.mongo")
@EnableRedisRepositories(basePackages = "com.thenriquedb.url_shortener.repositories.redis")
class RedisConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.url:redis://127.0.0.1:6379}") String redisUrl) {

        RedisURI uri = RedisURI.create(redisUrl);
        RedisStandaloneConfiguration standalone = new RedisStandaloneConfiguration();
        String host = uri.getHost();
        if (!StringUtils.hasText(host)) {
            host = "127.0.0.1";
        }
        standalone.setHostName(Objects.requireNonNull(host));
        standalone.setPort(uri.getPort());
        standalone.setDatabase(uri.getDatabase());

        RedisCredentialsProvider credentialsProvider = uri.getCredentialsProvider();
        if (credentialsProvider != null) {
            RedisCredentials creds = credentialsProvider.resolveCredentials().blockOptional().orElse(null);
            if (creds != null) {
                if (creds.hasUsername() && StringUtils.hasText(creds.getUsername())) {
                    standalone.setUsername(creds.getUsername());
                }
                if (creds.hasPassword()) {
                    char[] password = creds.getPassword();
                    if (password != null && password.length > 0) {
                        standalone.setPassword(RedisPassword.of(new String(password)));
                    }
                }
            }
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder client =
                LettuceClientConfiguration.builder();
        if (uri.isSsl()) {
            client.useSsl();
        }

        return new LettuceConnectionFactory(standalone, client.build());
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(Objects.requireNonNull(redisConnectionFactory));
    }
}

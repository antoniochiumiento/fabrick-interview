package com.fabrick.interview.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for the application's caching layer.
 * <p>
 * This class enables Spring's annotation-driven cache management and configures
 * <b>Caffeine</b> as the underlying cache provider. Caffeine is a high-performance,
 * near-optimal caching library.
 * </p>
 *
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Defines and configures the {@link CacheManager} bean.
     * <p>
     * The manager is configured with specific eviction policies to balance memory usage
     * and data freshness:
     * <ul>
     * <li><b>Maximum Size:</b> Limited to 100 entries to prevent memory overflows.</li>
     * <li><b>Expiration:</b> Entries expire 10 minutes after being written (TTL), ensuring data consistency with the source.</li>
     * <li><b>Async Mode:</b> Enabled to support non-blocking operations, making it compatible with the Reactive Stack (WebFlux).</li>
     * </ul>
     * </p>
     *
     * @return The configured {@link CaffeineCacheManager} instance.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("asteroids");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES));

        // Critical for WebFlux: allows the cache to return CompletableFuture
        // instead of blocking the thread.
        cacheManager.setAsyncCacheMode(true);

        return cacheManager;
    }
}

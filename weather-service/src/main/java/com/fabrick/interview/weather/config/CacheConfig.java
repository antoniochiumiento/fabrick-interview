package com.fabrick.interview.weather.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


/**
 * Configuration class for the caching layer of the Weather Service.
 * <p>
 * This class configures <b>Caffeine</b> as the backing cache provider. Caffeine is chosen for its
 * high performance and near-optimal eviction policies.
 * </p>
 * <p>
 * This implementation specifically addresses the "Bonus Point" requirement regarding local caching,
 * reducing the load on the external Aviation Weather API and improving response times.
 * </p>
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Defines and configures the {@link CacheManager} bean for "stations" and "airports".
     * <p>
     * The cache is configured with the following policies:
     * <ul>
     * <li><b>Maximum Size:</b> 500 entries. Limits memory usage to prevent overflows.</li>
     * <li><b>Expiration:</b> Entries expire 10 minutes after write (TTL). Ensures weather data remains relatively fresh.</li>
     * <li><b>Async Mode:</b> Enabled ({@code setAsyncCacheMode(true)}). This is crucial for compatibility with
     * the Reactive Stack (WebFlux), allowing cache operations to return {@code CompletableFuture} and avoiding thread blocking.</li>
     * </ul>
     * </p>
     *
     * @return A fully configured {@link CaffeineCacheManager} instance.
     */
    @Bean
    public CacheManager cacheManager() {
        // Initialize cache manager for specific cache names used in the service layer
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("stations", "airports");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES));

        // Enable async mode for WebFlux compatibility
        cacheManager.setAsyncCacheMode(true);

        return cacheManager;
    }
}
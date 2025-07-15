package com.example.ratelimiter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // This annotation can be applied to methods
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime
public @interface RateLimited {
    /**
     * The maximum number of requests allowed within the specified time unit.
     * @return The capacity of the token bucket or the maximum requests for other algorithms.
     */
    int capacity();

    /**
     * The duration in seconds for the rate limit window.
     * @return The window duration in seconds.
     */
    long refillSeconds(); // Renamed to windowSeconds for better generality

    /**
     * Specifies the rate limiting algorithm to use.
     * Defaults to TOKEN_BUCKET.
     */
    Algorithm algorithm() default Algorithm.TOKEN_BUCKET;

    enum Algorithm {
        TOKEN_BUCKET,
        LEAKY_BUCKET,
        FIXED_WINDOW,
        SLIDING_WINDOW_LOG
    }
}

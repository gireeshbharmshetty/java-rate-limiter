package com.gireesh.ratelimiter;

/**
 * Interface for a rate limiter.
 */
public interface RateLimiter {

    /**
     * Attempts to acquire a permit.
     *
     * @param clientId The identifier for the client requesting the permit.
     * @return true if the permit was acquired successfully, false otherwise.
     */
    boolean tryAcquire(String clientId);
}


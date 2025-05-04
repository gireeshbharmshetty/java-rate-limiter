package com.gireesh.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * A simple implementation of the Token Bucket algorithm for rate limiting.
 * This implementation is per client ID and thread-safe.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final long capacity; // Max tokens in the bucket
    private final long refillRate; // Tokens added per second
    private final ConcurrentHashMap<String, Bucket> buckets;

    /**
     * Inner class representing a token bucket for a specific client.
     */
    private static class Bucket {
        final long capacity;
        final long refillRate;
        final AtomicLong currentTokens;
        final AtomicLong lastRefillTimestamp;

        Bucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.currentTokens = new AtomicLong(capacity); // Start full
            this.lastRefillTimestamp = new AtomicLong(System.currentTimeMillis());
        }

        /**
         * Refills the bucket based on the time elapsed since the last refill.
         */
        void refill() {
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTimestamp.get();
            long timeElapsed = now - lastRefill;

            if (timeElapsed > 0) {
                // Only attempt refill if time has passed and we can acquire the lock (CAS)
                if (lastRefillTimestamp.compareAndSet(lastRefill, now)) {
                    long tokensToAdd = (timeElapsed / 1000) * refillRate;
                    if (tokensToAdd > 0) {
                        long newTokens = Math.min(capacity, currentTokens.get() + tokensToAdd);
                        currentTokens.set(newTokens);
                    }
                }
            }
        }

        /**
         * Tries to consume one token from the bucket.
         *
         * @return true if a token was consumed, false otherwise.
         */
        boolean tryConsume() {
            refill(); // Refill before attempting to consume
            long current = currentTokens.get();
            if (current > 0) {
                // Attempt to decrement token count atomically
                return currentTokens.compareAndSet(current, current - 1);
            }
            return false;
        }
    }

    /**
     * Constructor for TokenBucketRateLimiter.
     *
     * @param capacity   The maximum number of tokens the bucket can hold.
     * @param refillRate The number of tokens added to the bucket per second.
     */
    public TokenBucketRateLimiter(long capacity, long refillRate) {
        if (capacity <= 0 || refillRate <= 0) {
            throw new IllegalArgumentException("Capacity and refill rate must be positive.");
        }
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.buckets = new ConcurrentHashMap<>();
    }

    /**
     * Attempts to acquire a permit for the given client ID.
     *
     * @param clientId The identifier for the client requesting the permit.
     * @return true if the permit was acquired successfully, false otherwise.
     */
    @Override
    public boolean tryAcquire(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be null or empty.");
        }
        // Get or create the bucket for the client atomically
        Bucket bucket = buckets.computeIfAbsent(clientId, k -> new Bucket(this.capacity, this.refillRate));
        return bucket.tryConsume();
    }

    // Optional: Method to get current tokens for a client (for monitoring/testing)
    public long getCurrentTokens(String clientId) {
         Bucket bucket = buckets.get(clientId);
         if (bucket != null) {
             bucket.refill(); // Ensure it's up-to-date
             return bucket.currentTokens.get();
         }
         return 0; // Or capacity if bucket doesn't exist yet
    }
}


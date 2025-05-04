package com.gireesh.ratelimiter;

/**
 * Example usage of the TokenBucketRateLimiter.
 */
public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        // Create a rate limiter: Allows 10 requests per second
        RateLimiter limiter = new TokenBucketRateLimiter(10, 10);

        String clientId1 = "user-123";
        String clientId2 = "service-abc";

        // Simulate requests from client 1
        System.out.println("Simulating requests for client: " + clientId1);
        for (int i = 0; i < 15; i++) {
            boolean acquired = limiter.tryAcquire(clientId1);
            System.out.println("Request " + (i + 1) + ": " + (acquired ? "Acquired" : "Denied") + " - Current Tokens: " + ((TokenBucketRateLimiter)limiter).getCurrentTokens(clientId1));
            Thread.sleep(50); // Simulate some time between requests
        }

        System.out.println("\nWaiting for bucket to refill slightly...\n");
        Thread.sleep(1000); // Wait 1 second

        // Simulate more requests from client 1 after refill
        System.out.println("Simulating more requests for client: " + clientId1 + " after refill");
        for (int i = 0; i < 5; i++) {
            boolean acquired = limiter.tryAcquire(clientId1);
            System.out.println("Request " + (i + 16) + ": " + (acquired ? "Acquired" : "Denied") + " - Current Tokens: " + ((TokenBucketRateLimiter)limiter).getCurrentTokens(clientId1));
            Thread.sleep(50);
        }

        // Simulate requests from client 2 (independent bucket)
        System.out.println("\nSimulating requests for client: " + clientId2);
        for (int i = 0; i < 5; i++) {
            boolean acquired = limiter.tryAcquire(clientId2);
            System.out.println("Request " + (i + 1) + ": " + (acquired ? "Acquired" : "Denied") + " - Current Tokens: " + ((TokenBucketRateLimiter)limiter).getCurrentTokens(clientId2));
            Thread.sleep(100);
        }
    }
}


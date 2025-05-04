# Java Token Bucket Rate Limiter

## Project Overview

This project provides a simple implementation of a Token Bucket rate limiter algorithm in Java. Rate limiting is a crucial technique used in distributed systems to control the rate of traffic sent or received by a network interface controller.

The Token Bucket algorithm is a popular choice because it allows for bursts of traffic while enforcing an average rate limit.

## How it Works

1.  **Token Bucket:** Imagine a bucket with a fixed capacity that holds tokens.
2.  **Token Refill:** Tokens are added to the bucket at a fixed rate (e.g., 10 tokens per second) up to the bucket's capacity.
3.  **Request Handling:** When a request arrives, it needs to consume one token from the bucket to be processed.
    *   If a token is available, the request consumes it and is allowed.
    *   If no token is available, the request is denied (or queued, depending on the strategy).
4.  **Concurrency:** This implementation uses `synchronized` blocks to ensure thread safety when multiple threads try to consume tokens concurrently.

## Benefits & Relevance

*   **Understanding Rate Limiting:** Demonstrates a fundamental concept in building robust and scalable distributed systems.
*   **Algorithm Implementation:** Provides practical application of algorithms and concurrency control in Java.
*   **Interview Relevance:** Rate limiter design and implementation are common questions in software engineering interviews, especially for backend and system design roles.
*   **Burst Handling:** Unlike simpler algorithms (like leaky bucket), the token bucket naturally allows for short bursts of requests as long as enough tokens have accumulated.

## How to Use/Run

1.  **Compile:** Compile the Java files using a Java Development Kit (JDK):
    ```bash
    javac src/main/java/com/gireesh/ratelimiter/*.java
    ```
2.  **Run the Demo:** Execute the `RateLimiterDemo` class to see the rate limiter in action:
    ```bash
    java -cp src/main/java com.gireesh.ratelimiter.RateLimiterDemo
    ```
    The demo simulates multiple threads attempting to acquire tokens from the rate limiter, showing which requests are allowed and which are denied based on the token availability and refill rate.

## Files

*   `src/main/java/com/gireesh/ratelimiter/RateLimiter.java`: The interface defining the rate limiter operation (`tryConsume`).
*   `src/main/java/com/gireesh/ratelimiter/TokenBucketRateLimiter.java`: The concrete implementation using the Token Bucket algorithm.
*   `src/main/java/com/gireesh/ratelimiter/RateLimiterDemo.java`: A demonstration class showing concurrent access to the rate limiter.


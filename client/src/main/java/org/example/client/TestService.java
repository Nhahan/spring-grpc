package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final UserGrpcClient userGrpcClient;
    private final UserRestClient userRestClient;

    private static final int BATCH_SIZE = 10000;
    private static final int MAX_CONCURRENT_REQUESTS = 100;

    public String runPerformanceTest(int requestCount, int itemCount) {
        for (int i = 0; i < 3; i++) {
            try {
                userGrpcClient.sendUserData(10);
                userRestClient.sendUserData(10);
            } catch (Exception e) {
                log.warn("Warmup failed: {}", e.getMessage());
            }
        }

        System.out.println("Starting concurrent tests...");
        long startTime = System.nanoTime();

        // Run gRPC and REST tests concurrently
        CompletableFuture<TestResult> grpcFuture = CompletableFuture.supplyAsync(() ->
                runTest("gRPC", requestCount, itemCount, userGrpcClient::sendUserData));

        CompletableFuture<TestResult> restFuture = CompletableFuture.supplyAsync(() ->
                runTest("REST", requestCount, itemCount, userRestClient::sendUserData));

        // Wait for both test results
        TestResult grpcResult = grpcFuture.join();
        TestResult restResult = restFuture.join();

        double totalDuration = (System.nanoTime() - startTime) / 1_000_000_000.0;
        return formatResults(grpcResult, restResult, requestCount, itemCount, totalDuration);
    }

    private TestResult runTest(String testType, int requestCount, int itemCount,
                               ThrowingConsumer<Integer> testFunction) {
        long testStart = System.nanoTime();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // Use a separate semaphore for each test type
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);

        // Process requests in batches
        for (int batch = 0; batch < requestCount; batch += BATCH_SIZE) {
            int currentBatchSize = Math.min(BATCH_SIZE, requestCount - batch);
            List<CompletableFuture<Void>> batchFutures = new ArrayList<>(currentBatchSize);

            for (int i = 0; i < currentBatchSize; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        semaphore.acquire();
                        try {
                            testFunction.accept(itemCount);
                            successCount.incrementAndGet();
                        } finally {
                            semaphore.release();
                        }
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        log.error("{} request failed: {}", testType, e.getMessage());
                    }
                });
                batchFutures.add(future);
            }

            // Wait for the current batch to complete
            CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0])).join();

            int completedRequests = batch + currentBatchSize;
            log.info("{} progress: {}/{} requests completed",
                    testType, completedRequests, requestCount);
        }

        double duration = (System.nanoTime() - testStart) / 1_000_000_000.0;
        return new TestResult(testType, duration, successCount.get(), failCount.get());
    }

    private String formatResults(TestResult grpcResult,
                                 TestResult restResult,
                                 int testCount,
                                 int itemCount,
                                 double totalDuration) {
        return String.format("""
            Test Results (requests: %d, items: %d)
            Total test time: %.3f seconds
            
            gRPC Results:
            Total time: %.3f seconds
            Success: %d
            Failed: %d
            
            REST Results:
            Total time: %.3f seconds
            Success: %d
            Failed: %d""",
                testCount,
                itemCount,
                totalDuration,
                grpcResult.duration(),
                grpcResult.successCount(),
                grpcResult.failCount(),
                restResult.duration(),
                restResult.successCount(),
                restResult.failCount()
        );
    }

    private record TestResult(String type, double duration, int successCount, int failCount) {}

    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}

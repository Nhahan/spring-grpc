package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final UserGrpcClient userGrpcClient;
    private final UserRestClient userRestClient;

    public String runPerformanceTest(int testCount, int itemCount) {
        System.out.println("Warming up...");
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

        CompletableFuture<TestResult> grpcTest = CompletableFuture.supplyAsync(() -> {
            long testStart = System.nanoTime();
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            List<CompletableFuture<Void>> grpcFutures = IntStream.range(0, testCount)
                    .mapToObj(i -> CompletableFuture.runAsync(() -> {
                        try {
                            userGrpcClient.sendUserData(itemCount);
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failCount.incrementAndGet();
                            log.error("gRPC request failed: {}", e.getMessage());
                        }
                    }))
                    .toList();

            CompletableFuture.allOf(grpcFutures.toArray(new CompletableFuture[0])).join();
            double duration = (System.nanoTime() - testStart) / 1_000_000_000.0;

            return new TestResult("gRPC", duration, successCount.get(), failCount.get());
        });

        CompletableFuture<TestResult> restTest = CompletableFuture.supplyAsync(() -> {
            long testStart = System.nanoTime();
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);

            List<CompletableFuture<Void>> restFutures = IntStream.range(0, testCount)
                    .mapToObj(i -> CompletableFuture.runAsync(() -> {
                        try {
                            userRestClient.sendUserData(itemCount);
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failCount.incrementAndGet();
                            log.error("REST request failed: {}", e.getMessage());
                        }
                    }))
                    .toList();

            CompletableFuture.allOf(restFutures.toArray(new CompletableFuture[0])).join();
            double duration = (System.nanoTime() - testStart) / 1_000_000_000.0;

            return new TestResult("REST", duration, successCount.get(), failCount.get());
        });

        // wait for all tests to complete
        TestResult grpcResult = grpcTest.join();
        TestResult restResult = restTest.join();

        double totalDuration = (System.nanoTime() - startTime) / 1_000_000_000.0;

        return formatResults(grpcResult, restResult, testCount, itemCount, totalDuration);
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
}

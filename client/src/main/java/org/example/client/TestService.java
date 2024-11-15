package org.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserGrpcClient userGrpcClient;
    private final UserRestClient userRestClient;

    public String runPerformanceTest(int testCount, int itemCount) {
        System.out.println("Warming up...");
        for (int i = 0; i < 3; i++) {
            userGrpcClient.sendUserData(10);
            userRestClient.sendUserData(10);
        }

        System.out.println("Starting concurrent tests...");
        long startTime = System.nanoTime();

        List<CompletableFuture<String>> grpcFutures = IntStream.range(0, testCount)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> userGrpcClient.sendUserData(itemCount)))
                .toList();

        List<CompletableFuture<String>> restFutures = IntStream.range(0, testCount)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> userRestClient.sendUserData(itemCount)))
                .toList();

        List<CompletableFuture<TestResult>> allFutures = new ArrayList<>();

        grpcFutures.forEach(future ->
                allFutures.add(future.thenApply(result ->
                        new TestResult("gRPC", System.nanoTime())
                ))
        );

        restFutures.forEach(future ->
                allFutures.add(future.thenApply(result ->
                        new TestResult("REST", System.nanoTime())
                ))
        );

        // wait for all futures to complete
        CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();

        // get results
        List<TestResult> results = allFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        Map<String, DoubleSummaryStatistics> stats = results.stream()
                .collect(Collectors.groupingBy(
                        TestResult::type,
                        Collectors.summarizingDouble(r -> (r.timestamp - startTime) / 1_000_000_000.0)
                ));

        return formatResults(stats, testCount, itemCount);
    }

    private String formatResults(Map<String, DoubleSummaryStatistics> stats, int testCount, int itemCount) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Test(testCount: %d, itemCount: %d)\n", testCount, itemCount));

        stats.forEach((type, stat) -> {
            sb.append(String.format("""
                %s Results:
                Average: %.2f seconds
                Min: %.2f seconds
                Max: %.2f seconds
                Count: %d
                """,
                    type, stat.getAverage(), stat.getMin(), stat.getMax(), stat.getCount()
            ));
        });

        return sb.toString();
    }

    private record TestResult(String type, long timestamp) {}
}

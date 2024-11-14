package org.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserGrpcClient userGrpcClient;
    private final UserRestClient userRestClient;

    public String runPerformanceTest(int testCount, int itemCount) {
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<String>> grpcFutures = IntStream.range(0, testCount)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> userGrpcClient.sendUserData(itemCount)))
                .toList();

        List<CompletableFuture<String>> restFutures = IntStream.range(0, testCount)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> userRestClient.sendUserData(itemCount)))
                .toList();

        CompletableFuture<Void> allGrpc = CompletableFuture.allOf(grpcFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<Void> allRest = CompletableFuture.allOf(restFutures.toArray(new CompletableFuture[0]));

        allGrpc.join();
        long grpcEndTime = System.currentTimeMillis();
        allRest.join();
        long restEndTime = System.currentTimeMillis();

        long grpcDuration = grpcEndTime - startTime;
        long restDuration = restEndTime - grpcEndTime;

        return String.format("gRPC duration: %d ms\nREST duration: %d ms", grpcDuration, restDuration);
    }
}

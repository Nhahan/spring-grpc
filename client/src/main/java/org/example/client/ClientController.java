package org.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final UserGrpcClient userGrpcClient;
    private final UserRestClient userRestClient;
    private final TestService testService;

    @GetMapping("/grpc")
    public String grpc(@RequestParam(defaultValue = "999") int itemCount) {
        return userGrpcClient.sendUserData(itemCount);
    }

    @GetMapping("/rest")
    public String rest(@RequestParam(defaultValue = "999") int itemCount) {
        return userRestClient.sendUserData(itemCount);
    }

    @GetMapping("/test")
    public String test(@RequestParam(defaultValue = "1000") int testCount, @RequestParam(defaultValue = "999") int itemCount) {
        return testService.runPerformanceTest(testCount, itemCount);
    }
}

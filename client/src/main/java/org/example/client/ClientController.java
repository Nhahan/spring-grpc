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


    @GetMapping("/grpc")
    public String grpc(@RequestParam(defaultValue = "World") String name) {
        return userGrpcClient.sendUserData(name);
    }

    @GetMapping("/rest")
    public String rest(@RequestParam(defaultValue = "World") String name) {
        return userRestClient.sendUserData(name);
    }
}

package org.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final GrpcClientService grpcClientService;
    private final RestClientService restClientService;

    @GetMapping("/grpc")
    public String grpc(@RequestParam(defaultValue = "World") String name) {
        return grpcClientService.sendHello(name);
    }

    @GetMapping("/rest")
    public String rest(@RequestParam(defaultValue = "World") String name) {
        return restClientService.sendHello(name);
    }
}

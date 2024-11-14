package org.example.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.common.GreeterGrpc;
import org.example.common.HelloReply;
import org.example.common.HelloRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientApplication {

    @GrpcClient("grpc-server")
    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            HelloRequest request = HelloRequest.newBuilder().setName("World").build();
            HelloReply reply = greeterBlockingStub.sayHello(request);
            System.out.println("Greeting: " + reply.getMessage());
        };
    }
}

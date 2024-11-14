package org.example.client;

import org.example.common.GreeterGrpc;
import org.example.common.HelloReply;
import org.example.common.HelloRequest;
import org.springframework.stereotype.Service;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class ClientService {

    @GrpcClient("grpc-server")
    private GreeterGrpc.GreeterBlockingStub greeterBlockingStub;

    public String sendHello(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply = greeterBlockingStub.sayHello(request);
        return reply.getMessage();
    }
}

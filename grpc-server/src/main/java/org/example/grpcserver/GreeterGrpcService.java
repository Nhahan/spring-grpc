package org.example.grpcserver;

import net.devh.boot.grpc.server.service.GrpcService;
import org.example.common.GreeterGrpc;
import org.example.common.HelloReply;
import org.example.common.HelloRequest;
import io.grpc.stub.StreamObserver;

@GrpcService
public class GreeterGrpcService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String message = "Hello, " + request.getName();
        HelloReply reply = HelloReply.newBuilder().setMessage(message).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}

package org.example.grpcserver;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.proto.UserGrpc;
import org.example.proto.UserRequest;
import org.example.proto.UserResponse;

@Slf4j
@GrpcService
public class GrpcServerService extends UserGrpc.UserImplBase {

    @Override
    public void sendUserData(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder responseBuilder = UserResponse.newBuilder();
        responseBuilder.setMessage(String.valueOf(request.getItemsCount()));

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

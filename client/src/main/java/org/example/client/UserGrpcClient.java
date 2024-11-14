package org.example.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.common.UserGrpc;
import org.example.common.UserRequest;
import org.example.common.UserResponse;
import org.example.strategy.GrpcUserRequestStrategy;
import org.example.strategy.UserRequestFactory;
import org.springframework.stereotype.Component;

@Component
public class UserGrpcClient {

    @GrpcClient("grpc-server")
    private UserGrpc.UserBlockingStub userStub;

    public String sendUserData(int itemCount) {
        UserRequestFactory<UserRequest> grpcFactory =
                new UserRequestFactory<>(new GrpcUserRequestStrategy());
        UserRequest request = grpcFactory.createUserRequest(itemCount);
        UserResponse response = userStub.sendUserData(request);
        return response.getMessage();
    }
}

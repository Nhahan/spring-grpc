package org.example.grpcserver;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.common.ItemSummary;
import org.example.common.UserGrpc;
import org.example.common.UserRequest;
import org.example.common.UserResponse;

@Slf4j
@GrpcService
public class GrpcServerService extends UserGrpc.UserImplBase {

    @Override
    public void sendUserData(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        log.info("Received gRPC request: {}", request.toString());

        UserResponse.Builder responseBuilder = UserResponse.newBuilder();
        responseBuilder.setMessage("User data processed successfully");

        for (var item : request.getItemsList()) {
            float totalPrice = item.getPrice() * item.getQuantity();
            ItemSummary itemSummary = ItemSummary.newBuilder()
                    .setItemId(item.getItemId())
                    .setName(item.getName())
                    .setTotalPrice(totalPrice)
                    .build();
            responseBuilder.addItemSummaries(itemSummary);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

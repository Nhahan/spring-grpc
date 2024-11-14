package org.example.client;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.common.*;
import org.springframework.stereotype.Component;

@Component
public class UserGrpcClient {

    @GrpcClient("grpc-server")
    private UserGrpc.UserBlockingStub userStub;

    public String sendUserData(String name) {
        UserRequest request = UserRequest.newBuilder()
                .setId("12345")
                .setUser(UserDetails.newBuilder()
                        .setUsername(name)
                        .setEmail(name + "@example.com")
                        .setAddress(Address.newBuilder()
                                .setStreet("123 Main St")
                                .setCity("Metropolis")
                                .setState("NY")
                                .setZip("12345")
                                .build())
                        .build())
                .addItems(Item.newBuilder()
                        .setItemId("item001")
                        .setName("Laptop")
                        .setDescription("A high-performance laptop")
                        .setPrice(999.99f)
                        .setQuantity(1)
                        .build())
                .addItems(Item.newBuilder()
                        .setItemId("item002")
                        .setName("Mouse")
                        .setDescription("Wireless mouse")
                        .setPrice(25.5f)
                        .setQuantity(2)
                        .build())
                .build();

        UserResponse response = userStub.sendUserData(request);
        return response.getMessage();
    }
}

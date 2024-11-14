package org.example.client.strategy;

import org.example.common.Address;
import org.example.common.Item;
import org.example.common.UserDetails;
import org.example.common.UserRequest;

public class GrpcUserRequestStrategy implements UserRequestStrategy<UserRequest> {

    @Override
    public UserRequest createUserRequest(int itemCount) {
        UserRequest.Builder requestBuilder = UserRequest.newBuilder()
                .setId("12345")
                .setUser(UserDetails.newBuilder()
                        .setUsername("defaultUser")
                        .setEmail("defaultUser@example.com")
                        .setAddress(Address.newBuilder()
                                .setStreet("123 Main St")
                                .setCity("Metropolis")
                                .setState("NY")
                                .setZip("12345")
                                .build())
                        .build());

        for (int i = 0; i < itemCount; i++) {
            requestBuilder.addItems(Item.newBuilder()
                    .setItemId("item" + i)
                    .setName("Item" + i)
                    .setDescription("Description for item " + i)
                    .setPrice(50)
                    .setQuantity(2)
                    .build());
        }
        return requestBuilder.build();
    }
}

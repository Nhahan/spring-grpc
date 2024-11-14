package org.example.client.strategy;

import org.example.client.dto.UserRequest;

import java.util.ArrayList;
import java.util.List;

public class RestUserRequestStrategy implements UserRequestStrategy<UserRequest> {

    @Override
    public UserRequest createUserRequest(int itemCount) {
        List<UserRequest.Item> items = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            items.add(UserRequest.Item.builder()
                    .itemId("item" + i)
                    .name("Item" + i)
                    .description("Description for item " + i)
                    .price(50)
                    .quantity(2)
                    .build());
        }

        return UserRequest.builder()
                .id("12345")
                .user(UserRequest.UserDetails.builder()
                        .username("defaultUser")
                        .email("defaultUser@example.com")
                        .address(UserRequest.Address.builder()
                                .street("123 Main St")
                                .city("Metropolis")
                                .state("NY")
                                .zip("12345")
                                .build())
                        .build())
                .items(items)
                .build();
    }
}

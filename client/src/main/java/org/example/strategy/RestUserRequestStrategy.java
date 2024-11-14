package org.example.strategy;

import org.example.dto.RestUserRequest;

import java.util.ArrayList;
import java.util.List;

public class RestUserRequestStrategy implements UserRequestStrategy<RestUserRequest> {

    @Override
    public RestUserRequest createUserRequest(int itemCount) {
        List<RestUserRequest.Item> items = new ArrayList<>();

        for (int i = 0; i < itemCount + 1; i++) {
            items.add(RestUserRequest.Item.builder()
                    .itemId("item" + i)
                    .name("Item" + i)
                    .description("Description for item " + i)
                    .price(50)
                    .quantity(2)
                    .build());
        }

        return RestUserRequest.builder()
                .id("12345")
                .user(RestUserRequest.UserDetails.builder()
                        .username("defaultUser")
                        .email("defaultUser@example.com")
                        .address(RestUserRequest.Address.builder()
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

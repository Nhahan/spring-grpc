package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.client.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserRestClient {

    private final RestTemplate restTemplate;

    @Value("${rest.client.rest-server.address}")
    private String serverUrl;

    public String sendUserData(String name) {
        UserRequest request = UserRequest.builder()
                .id("12345")
                .user(UserRequest.UserDetails.builder()
                        .username(name)
                        .email(name + "@example.com")
                        .address(UserRequest.Address.builder()
                                .street("123 Main St")
                                .city("Metropolis")
                                .state("NY")
                                .zip("12345")
                                .build())
                        .build())
                .item(UserRequest.Item.builder()
                        .itemId("item001")
                        .name("Laptop")
                        .description("A high-performance laptop")
                        .price(999.99f)
                        .quantity(1)
                        .build())
                .item(UserRequest.Item.builder()
                        .itemId("item002")
                        .name("Mouse")
                        .description("Wireless mouse")
                        .price(25.5f)
                        .quantity(2)
                        .build())
                .build();

        return restTemplate.postForObject(serverUrl + "/test", request, String.class);
    }
}

package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.client.dto.UserRequest;
import org.example.client.strategy.RestUserRequestStrategy;
import org.example.client.strategy.UserRequestFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserRestClient {

    private final RestTemplate restTemplate;

    @Value("${rest.client.rest-server.address}")
    private String serverUrl;

    public String sendUserData(int itemCount) {
        UserRequestFactory<UserRequest> restFactory =
                new UserRequestFactory<>(new RestUserRequestStrategy());
        UserRequest request = restFactory.createUserRequest(itemCount);
        return restTemplate.postForObject(serverUrl + "/test", request, String.class);
    }
}

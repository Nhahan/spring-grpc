package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.dto.RestUserRequest;
import org.example.dto.RestUserResponse;
import org.example.strategy.RestUserRequestStrategy;
import org.example.strategy.UserRequestFactory;
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
        UserRequestFactory<RestUserRequest> restFactory =
                new UserRequestFactory<>(new RestUserRequestStrategy());
        RestUserRequest request = restFactory.createUserRequest(itemCount);
        RestUserResponse response = restTemplate.postForObject(serverUrl + "/test", request, RestUserResponse.class);
        return response.getMessage();
    }
}

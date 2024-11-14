package org.example.restserver;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.RestUserRequest;
import org.example.dto.RestUserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RestServerController {

    @PostMapping("/test")
    public RestUserResponse sendUserData(@RequestBody RestUserRequest request) {
        return RestUserResponse.builder()
                .message(String.valueOf(request.getItems().size()))
                .build();
    }
}

package org.example.restserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class RestServerController {

    @PostMapping("/test")
    public String sendUserData(@RequestBody Map<Object, Object> request) {
        log.info("Received REST request: {}", request);
        return "User data processed successfully";
    }
}

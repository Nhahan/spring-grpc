package org.example.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestClientService {

    private final RestTemplate restTemplate;

    @Value("${rest.server.url}")
    private String serverUrl;

    public String sendHello(String name) {
        String url = serverUrl + "/greet?name=" + name;
        System.out.println("Sending request to: " + url);
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response: " + response);
        return response;
    }
}

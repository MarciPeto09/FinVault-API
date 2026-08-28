package com.Marci.FinVault.API.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class CognitoAuthController {

    @Value("${aws.cognito.domain:}")
    private String domain;

    @Value("${aws.cognito.client-id:}")
    private String clientId;

    @Value("${aws.cognito.client-secret:}")
    private String clientSecret;

    @Value("${aws.cognito.redirect-uri:http://localhost:5173}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/cognito/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing authorization code"));
        }

        String tokenUrl = "https://" + domain + "/oauth2/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", code);
        form.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body(Map.of("error", "Token exchange failed"));
        }

        Map<String, Object> tokenData = response.getBody();
        return ResponseEntity.ok(Map.of(
                "accessToken", tokenData.get("access_token"),
                "idToken", tokenData.get("id_token"),
                "refreshToken", tokenData.get("refresh_token"),
                "tokenType", tokenData.get("token_type")
        ));
    }
}

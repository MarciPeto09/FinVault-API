package com.Marci.FinVault.API.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Cognito has a custom logout URL.
 * See more information <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html">here</a>.
 */
@Component
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    @Value("${aws.cognito.domain:}")
    private String domain;

    @Value("${aws.cognito.logout-uri:http://localhost:5173/}")
    private String logoutRedirectUrl;

    @Value("${aws.cognito.user-pool-client-id:${COGNITO_CLIENT_ID:}}")
    private String userPoolClientId;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (!StringUtils.hasText(domain) || !StringUtils.hasText(userPoolClientId)) {
            return super.determineTargetUrl(request, response, authentication);
        }

        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
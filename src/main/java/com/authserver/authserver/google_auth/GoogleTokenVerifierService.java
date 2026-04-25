package com.authserver.authserver.google_auth;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.stereotype.Service;

@Service
public class GoogleTokenVerifierService {

    private final GoogleOAuthProperties properties;
    private GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierService(GoogleOAuthProperties properties) throws Exception {
        this.properties = properties;

        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(properties.getClientIds()) // VERY IMPORTANT
                .build();
    }

    public GoogleUser verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            Boolean emailVerified = (Boolean) payload.get("email_verified");

            if (emailVerified == null || !emailVerified) {
                throw new RuntimeException("Email not verified by Google");
            }
            return new GoogleUser(
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name"));

        } catch (Exception e) {
            throw new RuntimeException("Token verification failed", e);
        }
    }
}
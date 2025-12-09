package com.wok.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:./firebase-service-account.json}")
    private String firebaseConfigPath;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(firebaseConfigPath)
                );

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully");
            } catch (IOException e) {
                log.warn("Firebase config file not found at: {}. FCM notifications will be disabled.", firebaseConfigPath);
                return null;
            }
        }

        return FirebaseMessaging.getInstance();
    }
}

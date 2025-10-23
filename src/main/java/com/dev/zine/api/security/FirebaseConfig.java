package com.dev.zine.api.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    private static final String APP_NAME = "my-app";

    @Value("${app.environment}")
    private String env;

    @Value("${firebase.credentials.json:#{null}}")
    private String firebaseCredentialsJson;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        System.out.println("=== Firebase Configuration Debug ===");
        System.out.println("Environment: " + env);
        System.out.println("Firebase credentials from env var: " + (firebaseCredentialsJson != null ? "YES" : "NO"));
        
        InputStream serviceAccount;
        
        // Priority 1: Use environment variable if provided (best for production)
        if (firebaseCredentialsJson != null && !firebaseCredentialsJson.isEmpty()) {
            System.out.println("✓ Loading Firebase credentials from FIREBASE_CREDENTIALS_JSON environment variable");
            serviceAccount = new ByteArrayInputStream(firebaseCredentialsJson.getBytes());
        }
        // Priority 2: Use file path based on environment
        else if(env.equals("production")) {
            System.out.println("✓ Loading Firebase credentials from production file: /etc/secrets/zine-firebase-admin.json");
            String absolutePath = "/etc/secrets/zine-firebase-admin.json";
            serviceAccount = new FileInputStream(absolutePath);
        } else {
            String relativePath = "src/main/resources/zine-firebase-admin.json";
            String absolutePath = new File(relativePath).getAbsolutePath();
            System.out.println("✓ Loading Firebase credentials from local file: " + absolutePath);
            serviceAccount = new FileInputStream(absolutePath);
        }

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp app = null;
        if (FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(firebaseOptions, APP_NAME);
        } else {
            app = FirebaseApp.getInstance(APP_NAME);
        }

        return FirebaseMessaging.getInstance(app);
    }

}
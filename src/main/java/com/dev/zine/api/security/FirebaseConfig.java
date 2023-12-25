package com.dev.zine.api.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    private static final String APP_NAME = "my-app";

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {

        String relativePath = "src/main/resources/zine-firebase-admin.json";
        String absolutePath = new File(relativePath).getAbsolutePath();

        FileInputStream serviceAccount = new FileInputStream(absolutePath);

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
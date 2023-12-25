package com.dev.zine.utils;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component
// public class FirebaseConnectionTest implements CommandLineRunner {

// private final FirebaseMessaging firebaseMessaging;

// @Autowired
// public FirebaseConnectionTest(FirebaseMessaging firebaseMessaging) {
// this.firebaseMessaging = firebaseMessaging;
// }

// @Override
// public void run(String... args) {
// FirebaseApp firebaseApp = FirebaseApp.getInstance("my-app");
// if (firebaseApp != null) {
// String projectId = firebaseApp.getOptions().getProjectId();
// System.out.println("Connected to Firebase! Project ID: " +
// firebaseApp.getName()
// + firebaseApp.getOptions().getServiceAccountId());
// } else {
// System.out.println("Firebase connection is not established.");
// }
// }
// }
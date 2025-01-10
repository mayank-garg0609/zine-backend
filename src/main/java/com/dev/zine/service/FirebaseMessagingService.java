package com.dev.zine.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.model.NotifyT;
import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotifications(String token, NotifyT msg) throws FirebaseException {
        Notification notification = Notification.builder()
                .setTitle(msg.getTitle())
                .setBody(msg.getBody())
                .setImage(msg.getImageUrl())
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        firebaseMessaging.send(message);

    }

    public void sendNotificationToTopic(String topic, String subject, String content, String imageUrl, Map<String, String> data) {
        Notification notification = Notification.builder()
                .setTitle(subject)
                .setBody(content)
                .setImage(imageUrl)
                .build();

        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            firebaseMessaging.sendAsync(message).get();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void subscribeToTopic(List<String> token, String topic)
            throws InterruptedException, ExecutionException {
        firebaseMessaging.subscribeToTopicAsync(token, topic).get();

    }

    public void unsubscribeFromTopic(List<String> token, String topic) {
        try {
            firebaseMessaging.unsubscribeFromTopicAsync(token, topic).get();
        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();
        }

    }
}
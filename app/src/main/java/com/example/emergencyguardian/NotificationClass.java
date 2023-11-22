package com.example.emergencyguardian;

public class NotificationClass {

        private String title;
        private String body;

        public NotificationClass(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }


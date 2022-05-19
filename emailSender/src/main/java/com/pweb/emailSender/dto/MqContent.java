package com.pweb.emailSender.dto;

import java.io.Serializable;

public class MqContent implements Serializable {

    String title;
    String content;
    String email;

    public MqContent(String title, String content, String email) {
        this.title = title;
        this.content = content;
        this.email = email;
    }

    public MqContent() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

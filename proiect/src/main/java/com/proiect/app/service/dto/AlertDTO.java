package com.proiect.app.service.dto;

import java.io.Serializable;
import java.time.Instant;

public class AlertDTO implements Serializable {

    Long id;
    String title;
    String content;
    String email;

    public AlertDTO(Long id, String title, String content, Instant createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public AlertDTO() {}

    public AlertDTO(String title, String content, Instant createdDate) {
        this.title = title;
        this.content = content;
    }

    public AlertDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

package com.proiect.app.service.dto;

import com.proiect.app.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDTO {

    private String id;

    private String login;

    private String firstName;

    private String lastName;

    private String imageUrl;

    private PlaceDTO place;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user, PlaceDTO place) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();

        this.firstName = user.getFirstName();

        this.lastName = user.getLastName();

        this.imageUrl = user.getImageUrl();
        this.email = user.getEmail();
        this.place = place;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();

        this.firstName = user.getFirstName();

        this.lastName = user.getLastName();

        this.imageUrl = user.getImageUrl();
        this.email = user.getEmail();
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}

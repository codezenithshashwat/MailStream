package com.example.mailstream.model;

import jakarta.persistence.*;

import java.util.UUID;
//uuid is java 128 bit value used to uniquely identify objects, in this project it is required as i am working on system that handles many users

@Entity
@Table(name= "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(unique=true, nullable= false)
    private String email;

    @Column(nullable= false)
    private String firstName;
    private boolean isActive= true;

    public Subscriber() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


}

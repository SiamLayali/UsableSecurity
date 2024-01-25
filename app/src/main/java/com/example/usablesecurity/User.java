package com.example.usablesecurity;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String numberOfChildren;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String firstName, String lastName, String email, String phone, String numberOfChildren) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.numberOfChildren = numberOfChildren;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(String numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}

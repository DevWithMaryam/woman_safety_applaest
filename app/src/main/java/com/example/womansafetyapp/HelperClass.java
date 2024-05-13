package com.example.womansafetyapp;

public class HelperClass {
    String name;
    String email;
    String username;
    String password;
    String signupGmail; // New field for guardian's email
    String EmergencyNumber;
    public HelperClass(String name, String email, String username, String password, String signupGmail, String EmergencyNumber) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.signupGmail = signupGmail;
        this.EmergencyNumber = EmergencyNumber;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignupGmail() {
        return signupGmail;
    }
    public void setSignupGmail(String signupGmail) {
        this.signupGmail = signupGmail;
    }


    public String getEmergencyNumber() {
        return EmergencyNumber;
    }

    public void setEmergencyNumber(String EmergencyNumber ) {this.name = EmergencyNumber;
    }


    public HelperClass() {
    }
}

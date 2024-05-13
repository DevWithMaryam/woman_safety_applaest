package com.example.womansafetyapp;

public class HelperClassg {
    String name;
    String email;            //g means guardian
    String username;
    String password;

    public HelperClassg(String gname, String gemail, String gusername, String gpassword) {
        this.name = gname;
        this.email = gemail;
        this.username = gusername;
        this.password = gpassword;
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




    public HelperClassg() {
    }
}


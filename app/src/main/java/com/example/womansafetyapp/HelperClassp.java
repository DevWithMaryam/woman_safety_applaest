package com.example.womansafetyapp;

public class HelperClassp {  private String name;
    private String email;
    private String username;
    private String password;
    private String policenum;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLnogitude() {
        return lnogitude;
    }

    public void setLnogitude(Double lnogitude) {
        this.lnogitude = lnogitude;
    }

    private Double latitude;
    private Double lnogitude;


    // Constructor
    public HelperClassp(String name, String email, String username, String password,String policenumm,Double latitude,Double lnogitude) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.policenum=policenumm;
        this.latitude=latitude;
        this.lnogitude=lnogitude;
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

    public String getPolicenum() {return policenum;}

    public void setPolicenum(String policenum) {
        this.policenum = policenum;
    }

}

package com.codelipi.ecomm;

/**
 * Created by Manish on 8/21/2017.
 */

public class DataProvider {
    private String pin,state,city,street,email,address;

    public DataProvider(String pin, String state, String city, String street, String email, String address) {
        this.pin = pin;
        this.state = state;
        this.city = city;
        this.street = street;
        this.email = email;
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

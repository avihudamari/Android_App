package com.pjct.ddb.Entities;

import android.location.Location;

import com.google.firebase.database.Exclude;

public class User {

    String phone_number;
    String first_name;
    String last_name;
    String mail_address;
    Location address;

    public User(String phone_number, String first_name, String last_name, String mail_address, Location address) {
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail_address = mail_address;
        this.address = address;
    }

    public User() {
        this.phone_number = "";
        this.first_name = "";
        this.last_name = "";
        this.mail_address = "";
        this.address = new Location(" f");
    }

    @Exclude
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMail_address() {
        return mail_address;
    }

    public void setMail_address(String mail_address) {
        this.mail_address = mail_address;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone number=" + phone_number +
                ", first name=" + first_name +
                ", last name=" + last_name +
                ", mail=" + mail_address +
                ", address=" + address +
                '}';
    }


}

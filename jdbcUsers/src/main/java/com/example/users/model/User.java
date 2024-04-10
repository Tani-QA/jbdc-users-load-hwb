package com.example.users.model;
import java.util.Date;
public class User {
    private String login;
    private String password;
    private Date date;
    private String email;


    public User() {
    }

    public User(String login, String password, Date date, String email) {
        this.login = login;
        this.password = password;
        this.date = date;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Date getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

}

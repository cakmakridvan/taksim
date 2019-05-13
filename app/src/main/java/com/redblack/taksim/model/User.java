package com.redblack.taksim.model;

public class User {

    private String name;
    private String mail;
    private String mobilNo;

    public String getMobilNoUser() {
        return mobilNo;
    }

    public void setMobilNoUser(String mobilNo) {
        this.mobilNo = mobilNo;
    }

    public User(String name, String mail, String mobilNo) {
        this.name = name;
        this.mail = mail;
        this.mobilNo = mobilNo;
    }

    public String getNameUser() {
        return name;
    }

    public void setNameUser(String name) {
        this.name = name;
    }

    public String getMailUser() {
        return mail;
    }

    public void setMailUser(String mail) {
        this.mail = mail;
    }

}

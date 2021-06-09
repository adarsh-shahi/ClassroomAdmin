package com.example.campnewsadmin;

public class Notes {

    private String roll;
    private String name;
    private String profilepic;
    private String mail;
    private String subject;

    public Notes(String roll, String name, String profilepic, String mail) {
        this.roll = roll;
        this.name = name;
        this.profilepic = profilepic;
        this.mail = mail;
    }

    public Notes() {
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
